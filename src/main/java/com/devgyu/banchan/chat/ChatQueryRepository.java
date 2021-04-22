package com.devgyu.banchan.chat;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface ChatQueryRepository {
    Slice<Chat> findChatRoomAccountCounselorFetchBySessionId(String id, Pageable pageable);
}
