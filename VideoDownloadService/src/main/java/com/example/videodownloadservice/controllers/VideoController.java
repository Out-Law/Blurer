package com.example.videodownloadservice.controllers;

import com.example.videodownloadservice.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("/upload")
    public SseEmitter uploadVideo(@RequestParam("file") MultipartFile file) {
        return videoService.saveVideo(file);
    }

    @GetMapping("/videos/{fileName}")
    public void getVideo(@PathVariable String fileName){
        Resource video = videoService.loadVideo(fileName);
        ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getFilename() + "\"")
                .body(video);
    }
}
