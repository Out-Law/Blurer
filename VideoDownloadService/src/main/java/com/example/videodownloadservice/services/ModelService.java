package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.ModelInfo;
import com.example.videodownloadservice.enums.ObjectType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final Map<ObjectType, ModelInfo> objectModels = new ConcurrentHashMap<>();

    @Value("${model.directory}")
    private String modelsDirectoryPath;

    @PostConstruct
    private void initDirs() {
        createDirectory(modelsDirectoryPath);
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
    public void saveModel(MultipartFile proto, MultipartFile caffe, ObjectType objectType) {
        if (proto.isEmpty() || caffe.isEmpty()) {
            throw new RuntimeException();
        }

        File modelsDirectory = new File(modelsDirectoryPath);

        String basePath = modelsDirectory.getAbsolutePath() + File.separator;

        String protoFilename = generateFilename("proto_", proto.getOriginalFilename(), objectType);
        String caffeFilename = generateFilename("caffe_", caffe.getOriginalFilename(), objectType);

        Files.deleteIfExists(Path.of(basePath + protoFilename));
        Files.deleteIfExists(Path.of(basePath + caffeFilename));

        File protoFile = new File(basePath + protoFilename);
        File caffeFile = new File(basePath + caffeFilename);

        proto.transferTo(protoFile);
        caffe.transferTo(caffeFile);

        objectModels.put(objectType, ModelInfo.builder()
                        .modelsPath(modelsDirectoryPath)
                        .protoFile(protoFilename)
                        .caffe(caffeFilename)
                        .build());
    }

    public Optional<ModelInfo> getModelInfo(ObjectType objectType) {
        return Optional.ofNullable(objectModels.get(objectType));
    }

    private String generateFilename(String prefix, String filename, ObjectType objectType) {
        String[] filenameParts = filename.split("\\.");
        if (filenameParts.length == 0) {
            return objectType.name().toLowerCase();
        }
        return prefix + objectType.name().toLowerCase() + "." + filenameParts[filenameParts.length - 1];
    }
}
