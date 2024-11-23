package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.VideoConvertRequest;
import com.example.videodownloadservice.enums.NotificationType;
import com.example.videodownloadservice.services.VideoNotificationService.SendNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class VideoConverterService {

    private final WebClient videoConverterServiceWebClient;

    public String sendToConvert(VideoConvertRequest videoConvertRequest) {
        return videoConverterServiceWebClient.post()
                .uri("/convert")
                .bodyValue(videoConvertRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
