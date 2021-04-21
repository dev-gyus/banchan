package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.devgyu.banchan.account.chatroom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;

    public Chat addNewMessage(String sessionId, String message, ChatRole chatRole) {
        List<ChatRoom> tempChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = tempChatRoom.get(0);
        return new Chat(chatRoom, message, chatRole);
    }
}
