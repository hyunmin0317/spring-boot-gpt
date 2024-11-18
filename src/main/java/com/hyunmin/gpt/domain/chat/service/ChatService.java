package com.hyunmin.gpt.domain.chat.service;

import com.hyunmin.gpt.domain.chat.dto.ChatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final WebClient webClient;

    public Flux<String> streamChat(ChatRequest request) {
        return webClient.post()
                .bodyValue(request.toRequest())
                .retrieve()
                .bodyToFlux(String.class);
    }
}
