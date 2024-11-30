package com.hyunmin.gpt.domain.chat.service;

import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatResponseDto;
import com.hyunmin.gpt.domain.chat.dto.ChatUpdateRequestDto;
import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.chat.repository.ChatRepository;
import com.hyunmin.gpt.global.common.entity.Member;
import com.hyunmin.gpt.global.common.repository.MemberRepository;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatCommandService {

    private final ChatQueryService chatQueryService;
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public ChatResponseDto createChat(Long memberId, ChatRequestDto requestDto) {
        Chat chat = requestDto.toEntity();
        if (memberId != null) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
            chat.setMember(member);
        }
        return ChatResponseDto.from(chatRepository.save(chat));
    }

    public String getOrCreateChatId(Long memberId, ChatRequestDto requestDto) {
        return requestDto.chatId() != null ?
                chatQueryService.readChat(memberId, requestDto.chatId()).id() :
                createChat(memberId, requestDto).id();
    }

    public ChatResponseDto updateChat(Long memberId, String chatId, ChatUpdateRequestDto requestDto) {
        Chat chat = chatRepository.findByIdAndMemberId(chatId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.CHAT_NOT_FOUND));
        chat.update(requestDto.name());
        return ChatResponseDto.from(chat);
    }

    public void deleteChat(Long memberId, String chatId) {
        Chat chat = chatRepository.findByIdAndMemberId(chatId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.CHAT_NOT_FOUND));
        chatRepository.delete(chat);
    }
}
