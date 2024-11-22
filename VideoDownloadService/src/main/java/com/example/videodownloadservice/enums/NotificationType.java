package com.example.videodownloadservice.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {
    VIDEO_CONVERTED("Video successfully converted");

    private final String message;
}
