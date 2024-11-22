package com.example.videodownloadservice.services;

import com.example.videodownloadservice.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class VideoNotificationService {

    private final SseEmitterService sseEmitterService;
    private final NotificationSenderService notificationSenderService;

    @EventListener
    public void sendNotification(SendNotificationEvent sendNotificationEvent) {
        SseEmitter emitter = sseEmitterService.getEmitter(sendNotificationEvent.filename);
        notificationSenderService.sendNotification(emitter, NotificationType.VIDEO_CONVERTED, NotificationType.VIDEO_CONVERTED.getMessage());
    }

    public static class SendNotificationEvent extends ApplicationEvent {

        NotificationType notificationType;
        String filename;

        public SendNotificationEvent(Object source, NotificationType notificationType, String filename) {
            super(source);
            this.notificationType = notificationType;
            this.filename = filename;
        }
    }
}
