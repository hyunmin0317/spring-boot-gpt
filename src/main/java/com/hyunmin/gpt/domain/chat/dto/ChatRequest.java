package com.hyunmin.gpt.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ChatRequest(
        @NotBlank(message = "질문을 입력해주세요.")
        String question
) {

    public ChatGptRequest toRequest() {
        ChatGptRequest.Message message = ChatGptRequest.Message.from("user", question);
        return ChatGptRequest.from(List.of(message));
    }
}
