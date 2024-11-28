package com.hyunmin.gpt.domain.message.repository;

import com.hyunmin.gpt.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long>, MessageQueryRepository {

}
