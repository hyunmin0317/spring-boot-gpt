package com.hyunmin.gpt.global.security.handler;

import com.hyunmin.gpt.global.exception.code.ErrorCode;
import com.hyunmin.gpt.global.security.exception.handler.JwtAuthenticationExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * 접근 권한이 없는 요청에 대한 오류 응답을 처리하는 핸들러 클래스
 * 사용자가 요청한 자원에 대한 접근 권한이 없는 경우
 */
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * 접근 거부 시 호출되는 메서드
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        JwtAuthenticationExceptionHandler.handleException(response, ex, ErrorCode.FORBIDDEN_EXCEPTION);
    }
}
