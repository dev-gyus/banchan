package com.devgyu.banchan.chatroom.query;

import com.devgyu.banchan.chatroom.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface ChatRoomQueryRepository {
    List<ChatRoom> findCounselorFetchBySessionId(String sessionId);

    Slice<ChatRoom> findAllByCounselorIsNull(Pageable pageable);

    List<ChatRoom> findAllByCounsellingAndCounselorId(Long counselorId);

    List<ChatRoom> findChatFetchBySessionId(String sessionId);

    List<ChatRoom> findAccountCounselorFetchWaitingOrCounsellingOrNewMessageAllByAccountId(Long accountId);

    List<ChatRoom> findAccountFetchBySessionId(String sessionId);

    List<ChatRoom> findAllByCounselorId(Long counselorId);

    List<ChatRoom> findAllByAccountId(Long accountId);
}
