package com.devgyu.banchan.chatroom;

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
        } else if(findChatRoom.getChatRoomStatus() == ChatRoomStatus.COMPLETED){
            throw new IllegalArgumentException("잘못된 요청입니다");
        } else{
            findChatRoom.setChatRoomReadStatus(ChatRoomReadStatus.COUNSELOR_READ);
            previousStatus = findChatRoom.getChatRoomStatus();
        }

        return previousStatus;
    }

    public List<ChatRoom> findChatFetchBySessionId(String sessionId) {
        return chatRoomRepository.findChatFetchBySessionId(sessionId);
    }

    public void enterSet(String role, String sessionId){
        List<ChatRoom> findChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = findChatRoom.get(0);
        if(role.equals("COUNSELOR")) {
            chatRoom.setCounselorEnter(true);
            if(chatRoom.getChatRoomStatus() == ChatRoomStatus.CUSTOMER_NEWMESSAGE){
                chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.COUNSELOR_READ);
            }
        }else{
            chatRoom.setCustomerEnter(true);
            if(chatRoom.getChatRoomStatus() == ChatRoomStatus.COUNSELOR_NEWMESSAGE){
                chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.CUSTOMER_READ);
            }
        }
    }

    public void changeRead(String role, String sessionId) {
        List<ChatRoom> findChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = findChatRoom.get(0);
        if(role.equals("COUNSELOR") && chatRoom.isCustomerEnter()) {
            chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.CUSTOMER_READ);
        }else if(!role.equals("COUNSELOR") && chatRoom.isCounselorEnter()){
            chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.COUNSELOR_READ);
        }
    }

    public void accountExit(Account account, String sessionId) {
        List<ChatRoom> findChatRoom = chatRoomRepository.findBySessionId(sessionId);
        ChatRoom chatRoom = findChatRoom.get(0);

        if(account instanceof Counselor){
            chatRoom.setCounselorEnter(false);
        }else{
            chatRoom.setCustomerEnter(false);
        }
    }
}
