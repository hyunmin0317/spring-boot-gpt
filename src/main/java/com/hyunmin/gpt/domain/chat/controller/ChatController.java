package com.hyunmin.gpt.domain.chat.controller;

import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.service.ChatGptService;
import com.hyunmin.gpt.domain.chat.service.ChatService;
import com.hyunmin.gpt.global.security.annotation.AuthMember;
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
    public ResponseEntity<Flux<String>> streamChat(@AuthMember Long memberId, @RequestBody @Valid ChatRequestDto requestDto) {
        String chatId = chatService.getOrCreateChatId(memberId, requestDto);
        Flux<String> responseFlux = chatGptService.streamChat(chatId, requestDto);
        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(responseFlux);
    }
}
