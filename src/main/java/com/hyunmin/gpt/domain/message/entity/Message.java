package com.hyunmin.gpt.domain.message.entity;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.message.entity.enums.Role;
import com.hyunmin.gpt.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Message extends BaseEntity {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String content;
}
