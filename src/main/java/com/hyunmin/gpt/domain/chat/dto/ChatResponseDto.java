package com.hyunmin.gpt.domain.chat.dto;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatResponseDto(
        String id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ChatResponseDto from(Chat chat) {
        return ChatResponseDto.builder()
                .id(chat.getId())
                .name(chat.getName())
                .createdAt(chat.getCreatedAt())
                .updatedAt(chat.getUpdatedAt())
                .build();
    }
}
