package com.example.videodownloadservice.services;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class NotificationSenderService {

    private final Executor executor = Executors.newCachedThreadPool();

    public <T extends Enum<T>> void sendNotification(SseEmitter emitter, T type, Object data) {
        val event = SseEmitter.event()
                .name(type.name())
                .data(data);
        
        executor.execute(() -> {
            try {
                emitter.send(event);
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
                throw new RuntimeException(e);
            }
        });
    }
}
