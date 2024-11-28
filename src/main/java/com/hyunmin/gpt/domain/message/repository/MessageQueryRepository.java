package com.hyunmin.gpt.domain.message.repository;

import com.hyunmin.gpt.domain.message.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MessageQueryRepository {

    Slice<Message> findAllMessages(String chatId, Pageable pageable);
}
