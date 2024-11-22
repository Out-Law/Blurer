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
    private final ApplicationEventPublisher eventPublisher;

    public void sendToConvert(VideoConvertRequest videoConvertRequest) {
        videoConverterServiceWebClient.post()
                .uri("/convert")
                .bodyValue(videoConvertRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess((v) -> eventPublisher.publishEvent(new SendNotificationEvent(this, NotificationType.VIDEO_CONVERTED, videoConvertRequest.getVideoPath())))
                .subscribe();
    }
}
