package com.hyunmin.gpt.domain.chat.repository;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, String>, ChatQueryRepository {

    Optional<Chat> findByIdAndMemberId(String id, Long member_id);
}
