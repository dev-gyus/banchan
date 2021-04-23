package com.devgyu.banchan.chatroom.query;

import com.devgyu.banchan.chatroom.ChatRoom;
import com.devgyu.banchan.chatroom.ChatRoomStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.chatroom.QChatRoom.chatRoom;
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
                        .and(chatRoom.chatRoomStatus.eq(ChatRoomStatus.WAITING).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELLING)).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELOR_NEWMESSAGE).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.CUSTOMER_NEWMESSAGE)))))
                .fetch();
    }

    @Override
    public Slice<ChatRoom> findAllByCounselorIsNull(Pageable pageable) {
        List<ChatRoom> result = queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account).fetchJoin()
                .where(chatRoom.counselor.isNull())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Slice<ChatRoom> chatRoomSlice;
        if(result.size() > pageable.getPageSize()){
            chatRoomSlice = new SliceImpl<>(result, pageable, true);
        }else{
            chatRoomSlice = new SliceImpl<>(result, pageable, false);
        }
        return chatRoomSlice;
    }

    @Override
    public List<ChatRoom> findAllByCounsellingAndCounselorId(Long counselorId) {
        return queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account).fetchJoin()
                .join(chatRoom.counselor, counselor).fetchJoin()
                .where(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELLING).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.CUSTOMER_NEWMESSAGE).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELOR_NEWMESSAGE))).and(counselor.id.eq(counselorId)))
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

    @Override
    public List<ChatRoom> findAccountCounselorFetchWaitingOrCounsellingOrNewMessageAllByAccountId(Long accountId) {
        return queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account).fetchJoin()
                .join(chatRoom.counselor, counselor).fetchJoin()
                .where(account.id.eq(accountId).and(chatRoom.chatRoomStatus.eq(ChatRoomStatus.WAITING).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELLING).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.COUNSELOR_NEWMESSAGE).or(chatRoom.chatRoomStatus.eq(ChatRoomStatus.CUSTOMER_NEWMESSAGE))))))
                .orderBy(chatRoom.regDate.desc())
                .fetch();
    }

    @Override
    public List<ChatRoom> findAccountFetchBySessionId(String sessionId) {
        return queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account).fetchJoin()
                .where(chatRoom.sessionId.eq(sessionId))
                .fetch();
    }

    @Override
    public List<ChatRoom> findAllByCounselorId(Long counselorId) {
        return queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.counselor, counselor)
                .where(counselor.id.eq(counselorId).and(chatRoom.chatRoomStatus.ne(ChatRoomStatus.COMPLETED)))
                .fetch();
    }

    @Override
    public List<ChatRoom> findAllByAccountId(Long accountId) {
        return queryFactory
                .selectFrom(chatRoom)
                .join(chatRoom.account, account)
                .where(account.id.eq(accountId).and(chatRoom.chatRoomStatus.ne(ChatRoomStatus.COMPLETED)).and(chatRoom.chatRoomStatus.ne(ChatRoomStatus.WAITING)))
                .fetch();
    }
}
