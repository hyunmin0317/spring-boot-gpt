package com.hyunmin.gpt.domain.chat.dto;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import jakarta.validation.constraints.NotBlank;

public record ChatRequestDto(
        String chatId,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {

    public Chat toEntity() {
        return Chat.builder()
                .name(content)
                .build();
    }
}
