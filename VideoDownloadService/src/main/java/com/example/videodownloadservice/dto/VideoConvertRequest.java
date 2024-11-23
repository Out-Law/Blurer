package com.example.videodownloadservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VideoConvertRequest {

    @JsonProperty("input_path")
    private final String originalVideosDirectory;
    @JsonProperty("output_path")
    private final String convertedVideosDirectory;
    @JsonProperty("filename")
    private final String filename;
}
