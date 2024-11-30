package com.hyunmin.gpt.domain.chat.controller;

import com.hyunmin.gpt.domain.chat.dto.ChatGptRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatResponseDto;
import com.hyunmin.gpt.domain.chat.service.ChatGptService;
import com.hyunmin.gpt.domain.chat.service.ChatService;
import com.hyunmin.gpt.domain.message.service.MessageService;
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

    private final ChatService chatService;
    private final ChatGptService chatGptService;
    private final MessageService messageService;

    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<Flux<String>> streamChat(@AuthMember Long memberId, @RequestBody @Valid ChatRequestDto requestDto) {
        String chatId = chatService.getOrCreateChatId(memberId, requestDto);
        ChatGptRequestDto gptRequestDto = messageService.readMessagesForChatGpt(chatId, requestDto.content());
        Flux<String> responseFlux = chatGptService.streamChat(chatId, gptRequestDto, requestDto.content());
        return ResponseEntity.ok()
                .header("X-Accel-Buffering", "no")
                .body(responseFlux);
    }

    @GetMapping
    public ResponseEntity<Slice<ChatResponseDto>> readChats(@AuthMember Long memberId, @ParameterObject Pageable pageable) {
        Slice<ChatResponseDto> responseDtoSlice = chatService.readChats(memberId, pageable);
        return ResponseEntity.ok(responseDtoSlice);
    }
}
