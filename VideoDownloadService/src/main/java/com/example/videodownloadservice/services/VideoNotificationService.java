package com.example.videodownloadservice.services;

import com.example.videodownloadservice.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class VideoNotificationService {

    private final SseEmitterService sseEmitterService;
    private final NotificationSenderService notificationSenderService;

    public void sendVideoConvertedNotification(String filename) {
        SseEmitter emitter = sseEmitterService.getEmitter(filename);
        notificationSenderService.sendNotification(emitter, NotificationType.VIDEO_CONVERTED, NotificationType.VIDEO_CONVERTED.getMessage());
    }
}
