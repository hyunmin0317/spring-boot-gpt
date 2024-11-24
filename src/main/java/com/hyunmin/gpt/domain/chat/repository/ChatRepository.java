package com.hyunmin.gpt.domain.chat.repository;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {

}
