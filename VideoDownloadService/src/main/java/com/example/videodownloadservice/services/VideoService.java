package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.VideoConvertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoConverterService videoConverterService;

    @Value("${video.directory.original}")
    private String originalVideosDirectory;

    @Value("${video.directory.converted}")
    private String convertedVideosDirectory;


    public String saveVideo(MultipartFile file) throws IOException {
        File dbDirectory = new File(originalVideosDirectory);

        if (!dbDirectory.exists()) {
            if (!dbDirectory.mkdirs()) {
                throw new IOException("Не удалось создать директорию: " + dbDirectory.getAbsolutePath());
            }
        }

        String filePath = dbDirectory.getAbsolutePath() + File.separator + file.getOriginalFilename();
        File dest = new File(filePath);

        file.transferTo(dest);

        videoConverterService.sendToConvert(VideoConvertRequest.builder()
                        .originalVideosDirectory(originalVideosDirectory)
                        .convertedVideosDirectory(convertedVideosDirectory)
                        .videoPath(dest.getName())
                .build());

        return filePath;
    }
}
