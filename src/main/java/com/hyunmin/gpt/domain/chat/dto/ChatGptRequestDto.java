package com.hyunmin.gpt.domain.chat.dto;

import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.entity.enums.Role;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record ChatGptRequestDto(
        String model,
        boolean stream,
        List<MessageDto> messages
) {

    @Builder
    public record MessageDto(Role role, String content) {

        private static MessageDto from(String content) {
            return MessageDto.builder()
                    .role(Role.user)
                    .content(content)
                    .build();
        }

        private static MessageDto from(Message message) {
            return MessageDto.builder()
                    .role(message.getRole())
                    .content(message.getContent())
                    .build();
        }

        private static List<MessageDto> from(List<Message> messageList) {
            return messageList.stream()
                    .map(MessageDto::from)
                    .toList();
        }

        private static List<MessageDto> from(List<Message> messageList, String content) {
            List<MessageDto> messageDtos = new ArrayList<>(from(messageList));
            messageDtos.add(ChatGptRequestDto.MessageDto.from(content));
            return messageDtos;
        }
    }

    public static ChatGptRequestDto from(List<Message> messageList, String content) {
        return ChatGptRequestDto.builder()
                .model("gpt-3.5-turbo-0125")
                .stream(true)
                .messages(MessageDto.from(messageList, content))
                .build();
    }
}
