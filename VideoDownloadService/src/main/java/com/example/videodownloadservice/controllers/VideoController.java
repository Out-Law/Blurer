package com.example.videodownloadservice.controllers;

import com.example.videodownloadservice.services.VideoNotificationService;
import com.example.videodownloadservice.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final VideoNotificationService videoNotificationService;

    @PostMapping("/upload")
    public SseEmitter uploadVideo(@RequestParam("file") MultipartFile file) {
        return videoService.saveVideo(file);
    }

    @PostMapping("/notify/converted")
    public void sendVideoConvertedNotification(@RequestParam String filename) {
        videoNotificationService.sendVideoConvertedNotification(filename);
    }
}
