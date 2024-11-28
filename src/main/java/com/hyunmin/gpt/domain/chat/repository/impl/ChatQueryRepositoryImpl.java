package com.hyunmin.gpt.domain.chat.repository.impl;

import com.hyunmin.gpt.domain.chat.entity.Chat;
import com.hyunmin.gpt.domain.chat.repository.ChatQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.hyunmin.gpt.domain.chat.entity.QChat.chat;

@RequiredArgsConstructor
public class ChatQueryRepositoryImpl implements ChatQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Chat> findAllChats(Long memberId, Pageable pageable) {
        List<Chat> chats = fetchChatsByMember(memberId, pageable);
        return toSlice(chats, pageable);
    }

    private List<Chat> fetchChatsByMember(Long memberId, Pageable pageable) {
        return queryFactory.selectFrom(chat)
                .where(memberIdEq(memberId))
                .orderBy(chat.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private Slice<Chat> toSlice(List<Chat> chats, Pageable pageable) {
        boolean hasNext = chats.size() > pageable.getPageSize();
        if (hasNext) {
            chats.remove(chats.size() - 1);
        }
        return new SliceImpl<>(chats, pageable, hasNext);
    }

    private BooleanExpression memberIdEq(Long memberId) {
        return memberId != null ? chat.member.id.eq(memberId) : null;
    }
}
