package com.hyunmin.gpt.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDto {

    private String content;

    public void addContent(String content) {
        this.content += content;
    }

    public static ChatResponseDto from(String content) {
        return ChatResponseDto.builder()
                .content(content)
                .build();
    }
}
