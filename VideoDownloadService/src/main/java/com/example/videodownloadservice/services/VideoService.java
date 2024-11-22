package com.example.videodownloadservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class VideoService {

    private final String uploadDir = "/Users/out_law/AppJava/VideoDownloadService/VideoDownloadService/";

    public String saveVideo(MultipartFile file) throws IOException {
        File dbDirectory = new File(uploadDir + "db/");

        if (!dbDirectory.exists()) {
            if (!dbDirectory.mkdirs()) {
                throw new IOException("Не удалось создать директорию: " + dbDirectory.getAbsolutePath());
            }
        }

        String filePath = dbDirectory.getAbsolutePath() + File.separator + file.getOriginalFilename();
        File dest = new File(filePath);

        file.transferTo(dest);

        return filePath;
    }
}
