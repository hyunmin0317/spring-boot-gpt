package com.hyunmin.gpt.domain.message.service;

import com.hyunmin.gpt.domain.chat.dto.ChatGptRequestDto;
import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.chat.repository.ChatRepository;
import com.hyunmin.gpt.domain.message.dto.MessageRequestDto;
import com.hyunmin.gpt.domain.message.dto.MessageResponseDto;
import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.repository.MessageRepository;
import com.hyunmin.gpt.global.exception.GeneralException;
import com.hyunmin.gpt.global.exception.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    public Slice<MessageResponseDto> readMessages(String chatId, Pageable pageable) {
        Slice<Message> messageSlice = messageRepository.findAllMessages(chatId, pageable);
        return MessageResponseDto.from(messageSlice);
    }

    public ChatGptRequestDto readMessagesForChatGpt(String chatId, String content) {
        List<Message> messageList = messageRepository.findAllByChatId(chatId);
        return ChatGptRequestDto.from(messageList, content);
    }

    @Transactional
    public MessageResponseDto saveMessage(MessageRequestDto requestDto) {
        Chat chat = chatRepository.findById(requestDto.getChatId())
                .orElseThrow(() -> new GeneralException(ErrorCode.CHAT_NOT_FOUND));
        Message message = requestDto.toEntity();
        message.setChat(chat);
        return MessageResponseDto.from(messageRepository.save(message));
    }
}
