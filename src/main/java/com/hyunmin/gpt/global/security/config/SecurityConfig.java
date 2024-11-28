package com.hyunmin.gpt.global.security.config;

import com.hyunmin.gpt.global.security.filter.JwtAuthenticationExceptionFilter;
import com.hyunmin.gpt.global.security.filter.JwtAuthenticationFilter;
import com.hyunmin.gpt.global.security.handler.JwtAccessDeniedHandler;
import com.hyunmin.gpt.global.security.handler.JwtAuthenticationEntryPoint;
import com.hyunmin.gpt.global.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 설정을 구성하는 클래스
 * HTTP 보안 설정, 인증 필터 추가 등 보안 관련 설정을 정의
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * BCrypt 암호화 방식으로 PasswordEncoder 빈을 생성
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 필터 체인 설정 구성
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // CSRF 보호 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        // HTTP 응답 헤더 설정
        http.headers(headersConfigurer -> headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        // 경로별 인가 작업
        http.authorizeHttpRequests(authorize -> authorize
                // H2 콘솔, Swagger UI, API 문서, Actuator 정보 조회에 대한 모든 접근 허용
                .requestMatchers("/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**", "/actuator/info").permitAll()
                // 계정 관련 API 요청에 대한 모든 접근 허용
                .requestMatchers("/api/v1/accounts/**").permitAll()
                // 채팅 API 요청에 대한 모든 접근 허용 (POST)
                .requestMatchers(HttpMethod.POST, "/api/v1/chat").permitAll()
                // 전체 사용자 정보 조회 API 관리자 권한만 접근 허용
                .requestMatchers("/api/v1/members").hasRole("ADMIN")
                // 나머지 모든 요청은 인증 필요
                .anyRequest().authenticated()
        );

        // JWT 기반 인증을 처리하기 위해 JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 이전에 추가
        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        // JWT 인증 예외 핸들링 필터 추가
        http.addFilterBefore(new JwtAuthenticationExceptionFilter(), JwtAuthenticationFilter.class);

        // 인증 및 인가 오류 핸들러 추가
        http.exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler())
        );

        return http.build();
    }
}
