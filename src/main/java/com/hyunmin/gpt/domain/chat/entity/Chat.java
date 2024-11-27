package com.hyunmin.gpt.domain.chat.entity;

import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.global.common.entity.BaseEntity;
import com.hyunmin.gpt.global.common.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {

    @Id
    @Column(name = "chat_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Message> messages = new ArrayList<>();
}
