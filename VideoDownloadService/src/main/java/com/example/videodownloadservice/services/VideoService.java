package com.example.videodownloadservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class VideoService {

    private final String uploadDir = "...";

    public String saveVideo(MultipartFile file) throws IOException {
        String filePath = uploadDir + file.getOriginalFilename();
        File dest = new File(filePath);
        dest.getParentFile().mkdirs();
        file.transferTo(dest);
        return filePath;
    }
}
