package com.hyunmin.gpt.domain.message.service;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.chat.repository.ChatRepository;
import com.hyunmin.gpt.domain.message.dto.MessageRequestDto;
import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.repository.MessageRepository;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageCommandService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public void saveMessage(MessageRequestDto requestDto) {
        Chat chat = chatRepository.findById(requestDto.getChatId())
                .orElseThrow(() -> new GeneralException(ErrorCode.CHAT_NOT_FOUND));
        Message message = requestDto.toEntity();
        message.setChat(chat);
        messageRepository.save(message);
    }
}
