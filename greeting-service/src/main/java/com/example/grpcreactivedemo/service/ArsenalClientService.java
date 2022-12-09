package com.example.grpcreactivedemo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ArsenalClientService {
    //TODO this can also be moved as single bean as it is common for both tile and arsenal
    private final WebClient arsenalWebClient;

    //TODO this can also be moved as single bean as it is common for both tile and arsenal
    public ArsenalClientService(WebClient.Builder webClientBuilder) {
        this.arsenalWebClient = webClientBuilder.baseUrl("https://play-preprod.iqvideo.in").build();
    }

    public Mono<String> getArsenalResponse() {

        return this.arsenalWebClient.get().uri(uriBuilder -> uriBuilder.path("/v1/health/status")
                .build())
                .retrieve().bodyToMono(String.class);
    }
}
