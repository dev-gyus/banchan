package com.devgyu.banchan.account.chatroom.query;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.devgyu.banchan.account.chatroom.ChatRoomStatus;
import com.devgyu.banchan.account.chatroom.QChatRoom;
import com.devgyu.banchan.chat.QChat;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.account.chatroom.QChatRoom.chatRoom;
import static com.devgyu.banchan.chat.QChat.chat;
import static com.devgyu.banchan.modules.counselor.QCounselor.counselor;

public class ChatRoomRepositoryImpl implements ChatRoomQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public ChatRoomRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ChatRoom> findCounselorFetchBySessionId(String sessionId) {
        return queryFactory
                .selectFrom(chatRoom)
                .leftJoin(chatRoom.counselor, counselor).fetchJoin()
                .where(chatRoom.sessionId.eq(sessionId)
                        .and(chatRoom.chatRoomStatus.eq(ChatRoomStatus.WAITING).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELLING))))
                .fetch();
    }

    @Override
    public Page<ChatRoom> findAllByWaiting(Long counselorId, Pageable pageable) {
        QueryResults<ChatRoom> result = queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account).fetchJoin()
                .where(chatRoom.chatRoomStatus.eq(ChatRoomStatus.WAITING))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public List<ChatRoom> findAllByCounsellingAndCounselorId(Long counselorId) {
        return queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account).fetchJoin()
                .join(chatRoom.counselor, counselor).fetchJoin()
                .where(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELLING).and(counselor.id.eq(counselorId)))
                .fetch();
    }

    @Override
    public List<ChatRoom> findChatFetchBySessionId(String sessionId) {
        return queryFactory
                .selectFrom(chatRoom)
                .leftJoin(chatRoom.chatList, chat).fetchJoin()
                .where(chatRoom.sessionId.eq(sessionId))
                .fetch();
    }
}
