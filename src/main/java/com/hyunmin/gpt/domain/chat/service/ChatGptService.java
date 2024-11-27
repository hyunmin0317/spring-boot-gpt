package com.hyunmin.gpt.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunmin.gpt.domain.chat.dto.ChatGptRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatGptResponseDto;
import com.hyunmin.gpt.domain.message.dto.MessageRequestDto;
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

    public Flux<String> streamChat(String chatId, ChatGptRequestDto request) {
        MessageRequestDto messageRequestDto = MessageRequestDto.from(chatId);

        return webClient.post()
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchangeToFlux(response -> handleResponse(response, chatId, messageRequestDto))
                .onErrorResume(WebClientResponseException.class, this::handleWebClientException)
                .doOnComplete(() -> saveMessageContent(messageRequestDto));
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

            String content = extractContent(originalResponse);
            messageRequestDto.addContent(content);
            return objectMapper.writeValueAsString(ChatGptResponseDto.of(chatId, content));
        } catch (JsonProcessingException ex) {
            log.error("[ERROR] {} : {}", ex.getClass(), ex.getMessage(), ex);
            return null;
        }
    }

    private String extractContent(String response) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(response);
        JsonNode contentNode = jsonNode.path("choices").get(0).path("delta").path("content");
        return contentNode.isMissingNode() ? "" : contentNode.asText();
    }

    private Flux<String> handleWebClientException(WebClientResponseException ex) {
        log.error("[ERROR] {} : {}", ex.getClass(), ex.getMessage(), ex);
        return Flux.error(new GeneralException(ErrorCode.CHAT_API_EXCEPTION));
    }

    private void saveMessageContent(MessageRequestDto messageRequestDto) {
        log.info("Saving message content: {}", messageRequestDto.getContent());
        // TODO: Implement actual save logic here
    }
}
