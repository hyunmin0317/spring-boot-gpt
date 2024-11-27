package com.hyunmin.gpt.domain.chat.service;

import com.hyunmin.gpt.domain.chat.dto.ChatRequestDto;
import com.hyunmin.gpt.domain.chat.dto.ChatResponseDto;
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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public ChatResponseDto readChat(Long memberId, String chatId) {
        Chat chat = chatRepository.findByIdAndMemberId(chatId, memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.CHAT_NOT_FOUND));
        return ChatResponseDto.from(chat);
    }

    @Transactional
    public ChatResponseDto createChat(Long memberId, ChatRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorCode.ACCOUNT_NOT_FOUND));
        Chat chat = requestDto.toEntity();
        chat.setMember(member);
        return ChatResponseDto.from(chatRepository.save(chat));
    }

    @Transactional
    public String getOrCreateChatId(Long memberId, ChatRequestDto requestDto) {
        return requestDto.chatId() != null ?
                readChat(memberId, requestDto.chatId()).id() : createChat(memberId, requestDto).id();
    }
}
