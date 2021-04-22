package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.chatroom.ChatRoom;
import com.devgyu.banchan.chatroom.ChatRoomReadStatus;
import com.devgyu.banchan.chatroom.ChatRoomRepository;
import com.devgyu.banchan.chatroom.ChatRoomStatus;
import com.devgyu.banchan.modules.counselor.Counselor;
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
        if(chatRoom.getChatRoomStatus() == ChatRoomStatus.COMPLETED){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        if(chatRole == ChatRole.COUNSELOR){
            chatRoom.setChatRoomStatus(ChatRoomStatus.COUNSELOR_NEWMESSAGE);
            chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.DEFAULT);
        }else{
            chatRoom.setChatRoomStatus(ChatRoomStatus.CUSTOMER_NEWMESSAGE);
            chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.DEFAULT);
        }
        return new Chat(chatRoom, message, chatRole);
    }

    public Chat addInfoMessage(String sessionId, String message) {
        List<ChatRoom> tempChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = tempChatRoom.get(0);
        if(chatRoom.getChatRoomStatus() != ChatRoomStatus.WAITING){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        return new Chat(chatRoom, message, ChatRole.INFO);
    }
}
