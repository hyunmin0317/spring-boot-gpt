package com.hyunmin.gpt.domain.chat.controller;

import com.hyunmin.gpt.domain.chat.dto.ChatGptRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatResponseDto;
import com.hyunmin.gpt.domain.chat.service.ChatCommandService;
import com.hyunmin.gpt.domain.chat.service.ChatGptService;
import com.hyunmin.gpt.domain.chat.service.ChatQueryService;
import com.hyunmin.gpt.domain.message.service.MessageQueryService;
import com.hyunmin.gpt.global.security.annotation.AuthMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatQueryService chatQueryService;
    private final ChatCommandService chatCommandService;
    private final ChatGptService chatGptService;
    private final MessageQueryService messageQueryService;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> streamChat(@AuthMember Long memberId, @RequestBody @Valid ChatRequestDto requestDto) {
        String chatId = chatCommandService.getOrCreateChatId(memberId, requestDto);
        ChatGptRequestDto gptRequestDto = messageQueryService.readMessagesForChatGpt(chatId, requestDto.content());
        Flux<String> responseFlux = chatGptService.streamChat(chatId, gptRequestDto, requestDto.content());
        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(responseFlux);
    }

    @GetMapping
    public ResponseEntity<Slice<ChatResponseDto>> readChats(@AuthMember Long memberId, @ParameterObject Pageable pageable) {
        Slice<ChatResponseDto> responseDtoSlice = chatQueryService.readChats(memberId, pageable);
        return ResponseEntity.ok(responseDtoSlice);
    }
}
