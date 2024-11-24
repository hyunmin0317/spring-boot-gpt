package com.hyunmin.gpt.domain.chat.dto;

import lombok.Builder;

@Builder
public record ChatGptResponseDto(
        String chatId,
        String content
) {

    public static ChatGptResponseDto of(String chatId, String content) {
        return ChatGptResponseDto.builder()
                .chatId(chatId)
                .content(content)
                .build();
    }
}
