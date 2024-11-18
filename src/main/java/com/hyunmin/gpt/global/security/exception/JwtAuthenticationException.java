package com.hyunmin.gpt.global.security.exception;

import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import lombok.Getter;

@Getter
public class JwtAuthenticationException extends GeneralException {

    public JwtAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
