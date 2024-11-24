package com.example.videodownloadservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VideoConvertRequest {

    private final String inputPath;
    private final String outputPath;
    private final String inputFileName;
    private final ModelInfo modelInfo;
}
