package com.devgyu.banchan.account.chatroom;

import com.devgyu.banchan.account.chatroom.query.ChatRoomQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomQueryRepository {
    List<ChatRoom> findBySessionId(String sessionId);

    boolean existsBySessionId(String sessionId);

    ChatRoom findOneBySessionId(String sessionId);
}
