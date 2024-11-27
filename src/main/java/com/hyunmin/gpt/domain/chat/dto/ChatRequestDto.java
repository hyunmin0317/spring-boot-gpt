package com.hyunmin.gpt.domain.chat.dto;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.message.entity.enums.Role;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatRequestDto(
        String chatId,
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {

    public ChatGptRequestDto toGptRequest() {
        ChatGptRequestDto.Message message = ChatGptRequestDto.Message.of(Role.user, content);
        return ChatGptRequestDto.from(List.of(message));
    }

    public Chat toEntity() {
        return Chat.builder()
                .name(content)
                .build();
    }
}
