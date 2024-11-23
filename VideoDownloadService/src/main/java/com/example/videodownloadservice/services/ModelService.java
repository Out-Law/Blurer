package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.ModelInfo;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelService {

    private Map<String, ModelInfo> models;

    @Value("${model.directory}")
    private String modelsDirectoryPath;

    @PostConstruct
    private void initDirs() {
        createDirectory(modelsDirectoryPath);
        addExistingModels();
    }

    private void addExistingModels() {
        File folder = new File(modelsDirectoryPath);
        File[] files = folder.listFiles();
        List<String> fileNames = Arrays.stream(files).map(File::getName).toList();
        models = new ConcurrentHashMap<>(fileNames.stream()
                .collect(
                        Collectors.groupingBy(
                                filename -> {
                                    return filename.substring(filename.indexOf('_') + 1, filename.lastIndexOf('.'));
                                },
                                Collectors.collectingAndThen(
                                    Collectors.toList(),
                                    (List<String> names) -> {
                                        return ModelInfo.builder()
                                            .modelsPath(modelsDirectoryPath)
                                            .caffe(names.stream().filter(name -> name.startsWith("caffe_")).findFirst().orElseThrow())
                                            .protoFile(names.stream().filter(name -> name.startsWith("proto_")).findFirst().orElseThrow())
                                            .build();
                                    }))));
        System.out.println(models);
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
    public void saveModel(MultipartFile proto, MultipartFile caffe, String objectType) {
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

        models.put(objectType, ModelInfo.builder()
                        .modelsPath(modelsDirectoryPath)
                        .protoFile(protoFilename)
                        .caffe(caffeFilename)
                        .build());
    }

    public Optional<ModelInfo> getModelInfo(String objectType) {
        return Optional.ofNullable(models.get(objectType));
    }

    public Set<String> getModelsNames() {
        return models.keySet();
    }

    private String generateFilename(String prefix, String filename, String objectType) {
        String[] filenameParts = filename.split("\\.");
        if (filenameParts.length == 0) {
            return objectType.toLowerCase();
        }
        return prefix + objectType.toLowerCase() + "." + filenameParts[filenameParts.length - 1];
    }
}
