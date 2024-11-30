package com.hyunmin.gpt.domain.chat.dto;

import jakarta.validation.constraints.NotBlank;

public record ChatUpdateRequestDto(
        @NotBlank(message = "내용을 입력해주세요.")
        String name
) {

}
