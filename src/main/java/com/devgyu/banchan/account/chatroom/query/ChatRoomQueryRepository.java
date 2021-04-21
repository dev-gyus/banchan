package com.devgyu.banchan.account.chatroom.query;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomQueryRepository {
    List<ChatRoom> findCounselorFetchBySessionId(String sessionId);

    Page<ChatRoom> findAllByWaiting(Long counselorId, Pageable pageable);

    List<ChatRoom> findAllByCounsellingAndCounselorId(Long counselorId);

    List<ChatRoom> findChatFetchBySessionId(String sessionId);
}
