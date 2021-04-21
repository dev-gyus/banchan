package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatQueryRepository {
    Slice<Chat> findChatRoomAccountCounselorFetchBySessionId(String id, Pageable pageable);
}
