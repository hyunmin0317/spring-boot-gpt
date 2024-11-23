package com.hyunmin.gpt.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunmin.gpt.domain.chat.dto.ChatGptResponseDto;
import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.dto.MessageRequestDto;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatGptService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public Flux<String> streamChat(String chatId, ChatRequestDto request) {
        MessageRequestDto messageRequestDto = MessageRequestDto.from(chatId);

        return webClient.post()
                .bodyValue(request.toRequest())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchangeToFlux(response -> handleResponse(response, chatId, messageRequestDto))
                .onErrorResume(WebClientResponseException.class, this::handleWebClientException);
    }

    private Flux<String> handleResponse(ClientResponse response, String chatId, MessageRequestDto messageRequestDto) {
        if (response.statusCode().is2xxSuccessful()) {
            return response.bodyToFlux(String.class)
                    .mapNotNull(originalResponse -> processResponse(originalResponse, chatId, messageRequestDto))
                    .filter(Objects::nonNull);
        } else {
            log.error("[ERROR] {} : {}", "GPT API response statusCode", response.statusCode());
            return Flux.error(new GeneralException(ErrorCode.CHAT_GPT_EXCEPTION));
        }
    }

    private String processResponse(String originalResponse, String chatId, MessageRequestDto messageRequestDto) {
        try {
            if ("[DONE]".equals(originalResponse)) {
                return objectMapper.writeValueAsString(originalResponse);
            }

            JsonNode jsonNode = objectMapper.readTree(originalResponse);
            String content = extractContent(jsonNode);
            String finishReason = extractFinishReason(jsonNode);

            messageRequestDto.addContent(content);
            if (isFinalResponse(finishReason)) {
                // TODO 답변 저장 로직 추가
                log.info("Save Message: {}", messageRequestDto);
            }
            return objectMapper.writeValueAsString(ChatGptResponseDto.of(chatId, content, finishReason));
        } catch (JsonProcessingException ex) {
            log.error("[ERROR] {} : {}", ex.getClass(), ex.getMessage(), ex);
            return null;
        }
    }

    private String extractContent(JsonNode jsonNode) {
        JsonNode contentNode = jsonNode.path("choices").get(0).path("delta").path("content");
        return contentNode.isMissingNode() ? "" : contentNode.asText();
    }

    private String extractFinishReason(JsonNode jsonNode) {
        JsonNode reasonNode = jsonNode.path("choices").get(0).path("finish_reason");
        return reasonNode.isNull() ? "proceeding" : reasonNode.asText();
    }

    private boolean isFinalResponse(String finishReason) {
        return "stop".equals(finishReason) || "length".equals(finishReason);
    }

    private Flux<String> handleWebClientException(WebClientResponseException ex) {
        log.error("[ERROR] {} : {}", ex.getClass(), ex.getMessage(), ex);
        return Flux.error(new GeneralException(ErrorCode.CHAT_API_EXCEPTION));
    }
}
