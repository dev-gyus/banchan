package com.devgyu.banchan.account.chatroom.query;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatRoomQueryRepository {
    List<ChatRoom> findCounselorFetchBySessionId(String sessionId);

    Slice<ChatRoom> findAllByWaiting(Pageable pageable);

    List<ChatRoom> findAllByCounsellingAndCounselorId(Long counselorId);

    List<ChatRoom> findChatFetchBySessionId(String sessionId);

    List<ChatRoom> findAccountCounselorFetchWaitingOrCounsellingAllByAccountId(Long accountId);
}
