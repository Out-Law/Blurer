package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.VideoConvertRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoConverterService videoConverterService;
    private final SseEmitterService sseEmitterService;

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
    public SseEmitter saveVideo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException();
        }

        File originalVideosDirectory = new File(originalVideosDirectoryPath);

        String filePath = originalVideosDirectory.getAbsolutePath() + File.separator + generateFilename(file.getOriginalFilename());
        File dest = new File(filePath);

        file.transferTo(dest);

        videoConverterService.sendToConvert(VideoConvertRequest.builder()
                        .originalVideosDirectory(originalVideosDirectoryPath)
                        .convertedVideosDirectory(convertedVideosDirectoryPath)
                        .videoPath(dest.getName())
                .build());

        return sseEmitterService.createEmitter(dest.getName());
    }

    private String generateFilename(String originalFilename) {
        String[] filenameParts = originalFilename.split("\\.");
        return UUID.randomUUID() + "." + filenameParts[filenameParts.length - 1];
    }
}
