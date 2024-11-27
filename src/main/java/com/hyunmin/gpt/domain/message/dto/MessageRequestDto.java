package com.hyunmin.gpt.domain.message.dto;

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
    private String content;

    public void addContent(String content) {
        this.content += content;
    }

    public static MessageRequestDto from(String chatId) {
        return MessageRequestDto.builder()
                .chatId(chatId)
                .content("")
                .build();
    }
}
