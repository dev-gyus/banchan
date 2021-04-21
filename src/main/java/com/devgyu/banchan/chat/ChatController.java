package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.devgyu.banchan.account.chatroom.ChatRoomRepository;
import com.devgyu.banchan.account.chatroom.ChatRoomService;
import com.devgyu.banchan.account.chatroom.ChatRoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messageSender;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @GetMapping("/customer-service")
    public String customerService(@CurrentUser Account account, Model model, HttpSession session, @PageableDefault Pageable pageable){
        boolean infoSent = false;
        String sessionId = session.getId();
        List<ChatRoom> findChatRoomList = chatRoomService.findChatFetchBySessionId(sessionId);
        if (findChatRoomList.isEmpty()) {
            chatRoomService.createChatRoom(account.getId(), sessionId);
        } else {
            Slice<Chat> tempChatRoom = chatRepository.findChatRoomAccountCounselorFetchBySessionId(sessionId, pageable);
            if(!tempChatRoom.isEmpty()){
                List<Chat> chatList = tempChatRoom.getContent();
                ChatRoom chatRoom = chatList.get(0).getChatRoom();

                String accountNickname = chatRoom.getAccount().getNickname();
                String counselorNickname = chatRoom.getCounselor().getNickname();

                model.addAttribute("accountNickname", accountNickname);
                model.addAttribute("counselorNickname", counselorNickname);
                model.addAttribute("hasNext", tempChatRoom.hasNext());
                model.addAttribute("chatList", chatList);
                model.addAttribute("previousStatus", chatRoom.getChatRoomStatus());
                infoSent = true;
            }
        }
        model.addAttribute("infoSent", infoSent);
        model.addAttribute("sessionId", session.getId());
        return "chat/main";
    }
    @MessageMapping("/chat/{sessionId}/create")
    @SendTo("/queue/chat/{sessionId}")
    public ChatDto chat_create(@Payload Chat chat, SimpMessageHeaderAccessor accessor){
            Principal principal = accessor.getUser();
            String email = principal.getName();
            String nickname = (String) accessor.getSessionAttributes().get(email);
            String sessionId = (String) accessor.getSessionAttributes().get("sessionId");
            String message = nickname + "님 어서오세요. 곧 담당 상담사가 도착예정입니다.";

            Chat newChat = chatService.addNewMessage(sessionId, message, ChatRole.INFO);

            return new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole());
    }

    @MessageMapping("/chat/{sessionId}")
    @SendTo("/queue/chat/{sessionId}")
    public ChatDto chatting(@Payload Chat chat, SimpMessageHeaderAccessor accessor){
        Principal principal = accessor.getUser();
        String email = principal.getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);
        String sessionId = (String) accessor.getSessionAttributes().get("sessionId");

        Chat newChat = chatService.addNewMessage(sessionId, chat.getMessage(), ChatRole.NORMAL);
        return new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole());

    }
}
