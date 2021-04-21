package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.QAccount;
import com.devgyu.banchan.account.chatroom.QChatRoom;
import com.devgyu.banchan.modules.counselor.QCounselor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.account.chatroom.QChatRoom.chatRoom;
import static com.devgyu.banchan.chat.QChat.chat;
import static com.devgyu.banchan.modules.counselor.QCounselor.counselor;

public class ChatRepositoryImpl implements ChatQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public ChatRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Slice<Chat> findChatRoomAccountCounselorFetchBySessionId(String sessionId, Pageable pageable) {
        List<Chat> result = queryFactory
                .selectFrom(chat)
                .join(chat.chatRoom, chatRoom).fetchJoin()
                .join(chatRoom.account, account).fetchJoin()
                .leftJoin(chatRoom.counselor, counselor).fetchJoin()
                .where(chatRoom.sessionId.eq(sessionId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .orderBy(chat.sendDate.desc())
                .fetch();
        result.sort(new Comparator<Chat>() {
            @Override
            public int compare(Chat o1, Chat o2) {
                if(o1.getSendDate().isAfter(o2.getSendDate())){
                    return 1;
                }else if(o1.getSendDate().isEqual(o2.getSendDate())){
                    return 0;
                }else{
                    return -1;
                }
            }
        });
        SliceImpl<Chat> chatList;
        if(result.size() > pageable.getPageSize()){
            chatList = new SliceImpl<>(result, pageable, true);
        }else{
            chatList = new SliceImpl<>(result, pageable, false);
        }

        return chatList;
    }
}
