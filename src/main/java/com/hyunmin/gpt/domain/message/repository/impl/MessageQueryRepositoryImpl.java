package com.hyunmin.gpt.domain.message.repository.impl;

import com.hyunmin.gpt.domain.message.entity.Message;
import com.hyunmin.gpt.domain.message.repository.MessageQueryRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.hyunmin.gpt.domain.message.entity.QMessage.message;

@RequiredArgsConstructor
public class MessageQueryRepositoryImpl implements MessageQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Message> findAllMessages(String chatId, Pageable pageable) {
        List<Message> messages = fetchMessagesByChatId(chatId, pageable);
        return toSlice(messages, pageable);
    }

    private List<Message> fetchMessagesByChatId(String chatId, Pageable pageable) {
        return queryFactory.selectFrom(message)
                .where(chatIdEq(chatId))
                .orderBy(message.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
    }

    private Slice<Message> toSlice(List<Message> chats, Pageable pageable) {
        boolean hasNext = chats.size() > pageable.getPageSize();
        if (hasNext) {
            chats.remove(chats.size() - 1);
        }
        return new SliceImpl<>(chats, pageable, hasNext);
    }

    private BooleanExpression chatIdEq(String chatId) {
        return chatId != null ? message.chat.id.eq(chatId) : null;
    }
}
