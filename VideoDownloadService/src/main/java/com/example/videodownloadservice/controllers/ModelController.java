package com.example.videodownloadservice.controllers;

import com.example.videodownloadservice.enums.ObjectType;
import com.example.videodownloadservice.services.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping("/upload")
    public void uploadModel(@RequestParam MultipartFile protoFile, @RequestParam MultipartFile caffe, @RequestParam ObjectType objectType) {
        modelService.saveModel(protoFile, caffe, objectType);
    }
}
