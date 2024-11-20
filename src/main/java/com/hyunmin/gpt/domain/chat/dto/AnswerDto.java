package com.hyunmin.gpt.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private String answer;

    public void addAnswer(String answer) {
        this.answer += answer;
    }

    public static AnswerDto from(String answer) {
        return AnswerDto.builder()
                .answer(answer)
                .build();
    }
}
