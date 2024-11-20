package com.hyunmin.gpt.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatRequestDto(
        @NotBlank(message = "프롬프트를 입력해주세요.")
        String prompt
) {

    public ChatGptRequestDto toRequest() {
        ChatGptRequestDto.Message message = ChatGptRequestDto.Message.from("user", prompt);
        return ChatGptRequestDto.from(List.of(message));
    }
}
