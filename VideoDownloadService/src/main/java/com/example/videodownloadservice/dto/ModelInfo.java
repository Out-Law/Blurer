package com.example.videodownloadservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ModelInfo {

    private final String modelsPath;
    private final String protoFile;
    private final String caffe;
}
