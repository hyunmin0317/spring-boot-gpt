package com.hyunmin.gpt.domain.chat.dto;

import lombok.Builder;

@Builder
public record ChatResponse(
        String sessionId,
        String answer,
        String finishReason
) {

    public static ChatResponse of(String sessionId, String answer, String finishReason) {
        return ChatResponse.builder()
                .sessionId(sessionId)
                .answer(answer)
                .finishReason(finishReason)
                .build();
    }
}
