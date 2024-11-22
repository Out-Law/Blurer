package com.example.videodownloadservice.services;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseEmitterService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String key) {
        SseEmitter emitter = new SseEmitter(300000L);

        emitter.onCompletion(() -> emitters.remove(key));
        emitter.onError(ex -> emitters.remove(key));
        emitter.onTimeout(() -> emitters.remove(key));

        emitters.put(key, emitter);

        return emitter;
    }

    public SseEmitter getEmitter(String key) {
        return Optional.ofNullable(emitters.get(key)).orElseThrow();
    }
}
