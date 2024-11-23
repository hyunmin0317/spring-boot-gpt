package com.hyunmin.gpt.domain.chat.dto;

import lombok.Builder;

@Builder
public record ChatGptResponseDto(
        String chatId,
        String content,
        String finishReason
) {

    public static ChatGptResponseDto of(String chatId, String content, String finishReason) {
        return ChatGptResponseDto.builder()
                .chatId(chatId)
                .content(content)
                .finishReason(finishReason)
                .build();
    }
}
