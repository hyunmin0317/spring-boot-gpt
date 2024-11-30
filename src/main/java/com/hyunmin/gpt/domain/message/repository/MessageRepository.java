package com.hyunmin.gpt.domain.message.repository;

import com.hyunmin.gpt.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageQueryRepository {

    List<Message> findAllByChatId(String chatId);
}
