package com.hyunmin.gpt.domain.message.dto;

import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.entity.enums.Role;
import lombok.Builder;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;

@Builder
public record MessageResponseDto(
        Long id,
        String chatId,
        Role role,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static MessageResponseDto from(Message message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .chatId(message.getChat().getId())
                .role(message.getRole())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .build();
    }

    public static Slice<MessageResponseDto> from(Slice<Message> messageSlice) {
        return messageSlice.map(MessageResponseDto::from);
    }
}
