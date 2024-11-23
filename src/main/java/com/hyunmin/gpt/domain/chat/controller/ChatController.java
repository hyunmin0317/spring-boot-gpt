package com.hyunmin.gpt.domain.chat.controller;

import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.service.ChatGptService;
import com.hyunmin.gpt.domain.chat.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final ChatGptService chatGptService;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> streamChat(@RequestBody @Valid ChatRequestDto request) {
        String chatId = request.chatId() != null ? request.chatId() : chatService.createChat(request).id();
        Flux<String> responseFlux = chatGptService.streamChat(chatId, request);
        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(responseFlux);
    }
}
