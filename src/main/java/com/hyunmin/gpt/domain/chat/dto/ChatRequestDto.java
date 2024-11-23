package com.hyunmin.gpt.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatRequestDto(
        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {

    public ChatGptRequestDto toRequest() {
        ChatGptRequestDto.Message message = ChatGptRequestDto.Message.from("user", content);
        return ChatGptRequestDto.from(List.of(message));
    }
}
