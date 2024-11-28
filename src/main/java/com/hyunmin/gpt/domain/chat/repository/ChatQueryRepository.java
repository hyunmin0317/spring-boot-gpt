package com.hyunmin.gpt.domain.chat.repository;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatQueryRepository {

    Slice<Chat> findAllChats(Long memberId, Pageable pageable);
}
