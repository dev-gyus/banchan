package com.devgyu.banchan.chat;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.chatroom.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final SimpMessageSendingOperations messageSender;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;
    private final AppProperties appProperties;

    @GetMapping("/customer-service-list")
    public String customer_service_list(@CurrentUser Account account, Model model, HttpSession session) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAccountCounselorFetchWaitingOrCounsellingOrNewMessageAllByAccountId(account.getId());
        List<ChatRoomDto> counsellingList = new ArrayList<>();
        List<ChatRoomDto> waitingList = new ArrayList<>();

        if (!chatRoomList.isEmpty()) {
            chatRoomList.forEach(c -> {
                if (c.getChatRoomStatus() == ChatRoomStatus.WAITING) {
                    waitingList.add(new ChatRoomDto(null, c.getSessionId(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()), c.getChatRoomStatus(), c.getChatRoomReadStatus()));
                } else {
                    if(c.getCounselor() != null) {
                        counsellingList.add(new ChatRoomDto(c.getCounselor().getNickname(), c.getSessionId(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()), c.getChatRoomStatus(), c.getChatRoomReadStatus()));
                    }else {
                        waitingList.add(new ChatRoomDto(null, c.getSessionId(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()), c.getChatRoomStatus(), c.getChatRoomReadStatus()));
                    }
                }
            });
        }else{
            return "redirect:/customer-service/" + session.getId();
        }

        model.addAttribute("accountId", account.getId());
        model.addAttribute("counsellingChatRoomList", counsellingList);
        model.addAttribute("waitingChatRoomList", waitingList);

        return "chat/list";
    }

    @GetMapping("/customer-service-create")
    public String customer_service_create(@CurrentUser Account account, Model model, HttpSession session){
        boolean isExist = chatRoomRepository.existsBySessionId(session.getId());
        boolean infoSent = false;
        if(!isExist) {
            chatRoomService.createChatRoom(account.getId(), session.getId());
            model.addAttribute("previousStatus", ChatRoomStatus.WAITING);
        }else{
            infoSent = true;
            return "redirect:/customer-service/" + session.getId();
        }
        model.addAttribute("infoSent", infoSent);
        model.addAttribute("sessionId", session.getId());
        model.addAttribute("host", appProperties.getHost());
        return "chat/main";
    }

    @GetMapping("/customer-service/{sessionId}")
    public String customerService(@CurrentUser Account account, Model model, @PathVariable String sessionId, @PageableDefault Pageable pageable) {
        boolean infoSent = false;
        List<ChatRoom> findChatRoomList = chatRoomService.findChatFetchBySessionId(sessionId);
        if (findChatRoomList.isEmpty()) {
            chatRoomService.createChatRoom(account.getId(), sessionId);
            model.addAttribute("previousStatus", ChatRoomStatus.WAITING);
        } else {
            Slice<Chat> tempChatRoom = chatRepository.findChatRoomAccountCounselorFetchBySessionId(sessionId, pageable);
            if (!tempChatRoom.isEmpty()) {
                List<Chat> chatList = tempChatRoom.getContent();
                ChatRoom chatRoom = chatList.get(0).getChatRoom();

                if(chatRoom.getChatRoomStatus() != ChatRoomStatus.WAITING){
                    chatRoom.setChatRoomReadStatus(ChatRoomReadStatus.CUSTOMER_READ);
                }

                String accountNickname = chatRoom.getAccount().getNickname();

                if(chatRoom.getCounselor() != null) {
                    String counselorNickname = chatRoom.getCounselor().getNickname();
                    model.addAttribute("counselorNickname", counselorNickname);
                }

                model.addAttribute("accountNickname", accountNickname);
                model.addAttribute("hasNext", tempChatRoom.hasNext());
                model.addAttribute("chatList", chatList);
                model.addAttribute("previousStatus", chatRoom.getChatRoomStatus());
                infoSent = true;
            }
        }
        model.addAttribute("host", appProperties.getHost());
        model.addAttribute("infoSent", infoSent);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("hasNewMessage", false);
        return "chat/main";
    }
    @GetMapping("/chat/api/{sessionId}/read-chat")
    @ResponseBody
    public ResponseEntity api_readChat(@PathVariable String sessionId, @RequestParam(defaultValue = "Init", required = false) String role){
        if(role.equals("Init")){
            chatRoomService.enterSet("CUSTOMER", sessionId);
        }else {
            chatRoomService.changeRead(role, sessionId);
        }
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/chat/{sessionId}/create")
    @SendTo({"/queue/chat/{sessionId}", "/topic/list"})
    public ChatDto chat_create(@Payload ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        String email = principal.getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);
        String sessionId = chatDto.getSessionId();
        String message = nickname + "님 어서오세요. 곧 담당 상담사가 도착예정입니다.";

        ChatRegDateDto chatRegDateDto = chatService.addInfoMessage(sessionId, message);
        Chat newChat = chatRegDateDto.getChat();

        return new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole(), sessionId, chatRegDateDto.getChatRoomRegDate());
    }

    @MessageMapping("/chat/{sessionId}")
    @SendTo({"/queue/chat/{sessionId}", "/topic/counselor/new-message"})
    public ChatDto chatting(@Payload ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        Principal principal = accessor.getUser();
        String email = principal.getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);
        String sessionId = chatDto.getSessionId();

        Chat newChat = chatService.addNewMessage(sessionId, chatDto.getMessage(), ChatRole.NORMAL);
        return new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole(), sessionId);

    }
}
