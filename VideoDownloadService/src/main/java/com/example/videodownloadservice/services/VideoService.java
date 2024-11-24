package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.VideoConvertRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoConverterService videoConverterService;
    private final ModelService modelService;

    @Value("${video.directory.original}")
    private String originalVideosDirectoryPath;

    @Value("${video.directory.converted}")
    private String convertedVideosDirectoryPath;

    @PostConstruct
    @SneakyThrows
    private void initDirs() {
        createDirectory(originalVideosDirectoryPath);
        createDirectory(convertedVideosDirectoryPath);
    }

    @SneakyThrows
    private void createDirectory(String directoryName) {
        File directory = new File(directoryName);

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Не удалось создать директорию: " + directory.getAbsolutePath());
            }
        }
    }


    @SneakyThrows
    public String saveVideo(MultipartFile file, String objectType) {
        if (file.isEmpty()) {
            throw new RuntimeException();
        }

        File originalVideosDirectory = new File(originalVideosDirectoryPath);

        String filePath = originalVideosDirectory.getAbsolutePath() + File.separator + generateFilename(file.getOriginalFilename());
        File dest = new File(filePath);

        file.transferTo(dest);

        String convertedFileName = videoConverterService.sendToConvert(VideoConvertRequest.builder()
                        .inputPath(originalVideosDirectoryPath)
                        .outputPath(convertedVideosDirectoryPath)
                        .modelInfo(modelService.getModelInfo(objectType).orElseThrow())
                        .inputFileName(dest.getName())
                .build());

        return convertedFileName;
    }

    @SneakyThrows
    public Resource loadVideo(String fileName) {

        Path filePath = Paths.get(convertedVideosDirectoryPath).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new RuntimeException();
        }

        return resource;
    }

    private String generateFilename(String originalFilename) {
        String[] filenameParts = originalFilename.split("\\.");
        return UUID.randomUUID() + "." + filenameParts[filenameParts.length - 1];
    }
}
