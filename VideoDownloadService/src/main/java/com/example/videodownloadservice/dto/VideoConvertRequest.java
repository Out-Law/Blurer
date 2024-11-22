package com.example.videodownloadservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class VideoConvertRequest {

    private final String originalVideosDirectory;
    private final String convertedVideosDirectory;
    private final String videoPath;
}
