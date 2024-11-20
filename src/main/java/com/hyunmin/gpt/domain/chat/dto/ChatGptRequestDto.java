package com.hyunmin.gpt.domain.chat.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record ChatGptRequestDto(
        String model,
        boolean stream,
        List<Message> messages
) {

    @Builder
    public record Message(String role, String content) {

        public static Message from(String role, String content) {
            return Message.builder()
                    .role(role)
                    .content(content)
                    .build();
        }
    }

    public static ChatGptRequestDto from(List<Message> messages) {
        return ChatGptRequestDto.builder()
                .model("gpt-3.5-turbo-0125")
                .stream(true)
                .messages(messages)
                .build();
    }
}
