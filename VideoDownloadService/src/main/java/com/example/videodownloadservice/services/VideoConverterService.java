package com.example.videodownloadservice.services;

import com.example.videodownloadservice.dto.VideoConvertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class VideoConverterService {

    private final WebClient videoConverterServiceWebClient;

    public void sendToConvert(VideoConvertRequest videoConvertRequest) {
        videoConverterServiceWebClient.post()
                .uri("/convert")
                .bodyValue(videoConvertRequest)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
