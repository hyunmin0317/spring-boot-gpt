package com.hyunmin.gpt.domain.message.dto;

import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDto {

    private String chatId;
    private Role role;
    private String content;

    public void addContent(String content) {
        this.content += content;
    }

    public static MessageRequestDto of(String chatId, Role role, String content) {
        return MessageRequestDto.builder()
                .chatId(chatId)
                .role(role)
                .content(content)
                .build();
    }

    public Message toEntity() {
        return Message.builder()
                .role(role)
                .content(content)
                .build();
    }
}
