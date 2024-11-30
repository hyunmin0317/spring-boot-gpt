package com.hyunmin.gpt.domain.message.controller;

import com.hyunmin.gpt.domain.message.dto.MessageResponseDto;
import com.hyunmin.gpt.domain.message.service.MessageQueryService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat/{chatId}/messages")
public class MessageController {

    private final MessageQueryService messageQueryService;

    @GetMapping
    public ResponseEntity<Slice<MessageResponseDto>> readMessages(@PathVariable String chatId, @ParameterObject Pageable pageable) {
        Slice<MessageResponseDto> responseDtoSlice = messageQueryService.readMessages(chatId, pageable);
        return ResponseEntity.ok(responseDtoSlice);
    }
}
