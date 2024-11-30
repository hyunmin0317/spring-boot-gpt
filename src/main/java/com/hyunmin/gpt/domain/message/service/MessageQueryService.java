package com.hyunmin.gpt.domain.message.service;

import com.hyunmin.gpt.domain.chat.dto.ChatGptRequestDto;
import com.hyunmin.gpt.domain.message.dto.MessageResponseDto;
import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageQueryService {

    private final MessageRepository messageRepository;

    public Slice<MessageResponseDto> readMessages(String chatId, Pageable pageable) {
        Slice<Message> messageSlice = messageRepository.findAllMessages(chatId, pageable);
        return MessageResponseDto.from(messageSlice);
    }

    public ChatGptRequestDto readMessagesForChatGpt(String chatId, String content) {
        List<Message> messageList = messageRepository.findAllByChatId(chatId);
        return ChatGptRequestDto.from(messageList, content);
    }
}
