package com.devgyu.banchan.account.chatroom;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.modules.counselor.Counselor;
import com.devgyu.banchan.modules.counselor.CounselorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final CounselorRepository counselorRepository;
    private final AccountRepository accountRepository;

    public void createChatRoom(Long accountId, String sessionId){
        Account account = accountRepository.findById(accountId).get();
        ChatRoom chatRoom = new ChatRoom(account, sessionId);
        chatRoomRepository.save(chatRoom);
    }

    public void counsellingChatRoom(String sessionId){
        List<ChatRoom> tempChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = tempChatRoom.get(0);
        chatRoom.setChatRoomStatus(ChatRoomStatus.COUNSELLING);
    }

    public void completedChatRoom(String sessionId){
        List<ChatRoom> tempChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = tempChatRoom.get(0);
        chatRoom.setChatRoomStatus(ChatRoomStatus.COMPLETED);
    }

    public boolean existBySessionId(String sessionId) {
        return chatRoomRepository.existsBySessionId(sessionId);
    }

    public ChatRoomStatus changeStatus(String sessionId, Counselor counselor) {
        List<ChatRoom> tempChatRoom = chatRoomRepository.findCounselorFetchBySessionId(sessionId);
        ChatRoom findChatRoom = tempChatRoom.get(0);
        if(findChatRoom.getCounselor() != null && !findChatRoom.getCounselor().getId().equals(counselor.getId())){
            throw new IllegalArgumentException("이미 상담중인 고객입니다.");
        }

        ChatRoomStatus previousStatus;
        if(findChatRoom.getChatRoomStatus() == ChatRoomStatus.WAITING) {
            Counselor findCounselor = counselorRepository.findById(counselor.getId()).get();
            previousStatus = findChatRoom.getChatRoomStatus();
            findChatRoom.changeCounselling(findCounselor);
        }else{
            previousStatus = findChatRoom.getChatRoomStatus();
        }

        return previousStatus;
    }

    public List<ChatRoom> findChatFetchBySessionId(String sessionId) {
        return chatRoomRepository.findChatFetchBySessionId(sessionId);
    }
}
