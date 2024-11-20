package com.hyunmin.gpt.domain.chat.dto;

import lombok.Builder;

@Builder
public record ChatResponseDto(
        String sessionId,
        String answer,
        String finishReason
) {

    public static ChatResponseDto of(String sessionId, String answer, String finishReason) {
        return ChatResponseDto.builder()
                .sessionId(sessionId)
                .answer(answer)
                .finishReason(finishReason)
                .build();
    }
}
