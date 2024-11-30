package com.hyunmin.gpt.domain.chat.service;

import com.hyunmin.gpt.domain.chat.dto.ChatResponseDto;
import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.chat.repository.ChatRepository;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatQueryService {

    private final ChatRepository chatRepository;

    public Slice<ChatResponseDto> readChats(Long memberId, Pageable pageable) {
        Slice<Chat> chatSlice = chatRepository.findAllChats(memberId, pageable);
        return ChatResponseDto.from(chatSlice);
    }

    public ChatResponseDto readChat(Long memberId, String chatId) {
        Chat chat = chatRepository.findByIdAndMemberId(chatId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.CHAT_NOT_FOUND));
        return ChatResponseDto.from(chat);
    }
}
