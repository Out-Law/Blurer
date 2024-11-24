package com.example.videodownloadservice.controllers;

import com.example.videodownloadservice.services.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping("/upload")
    public void uploadModel(@RequestParam MultipartFile protoFile, @RequestParam MultipartFile caffeFile, @RequestParam String objectType) {
        modelService.saveModel(protoFile, caffeFile, objectType);
    }

    @GetMapping
    public Set<String> getModelsNames() {
        return modelService.getModelsNames();
    }
}
