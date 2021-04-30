package com.devgyu.banchan.modules.counselor;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.chatroom.ChatRoom;
import com.devgyu.banchan.chatroom.ChatRoomRepository;
import com.devgyu.banchan.chatroom.ChatRoomService;
import com.devgyu.banchan.chatroom.ChatRoomStatus;
import com.devgyu.banchan.chat.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/counselor")
public class CounselorController {
    private final CounselorService counselorService;
    private final ChatService chatService;
    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final AppProperties appProperties;

    @GetMapping("/list")
    public String counselor_list(@CurrentUser Counselor counselor, @PageableDefault Pageable pageable, Model model) {
        Slice<ChatRoom> waitingChatRoomList = chatRoomRepository.findAllByCounselorIsNull(pageable);
        List<ChatRoom> counsellingChatRoomList = chatRoomRepository.findAllByCounsellingAndCounselorId(counselor.getId());

        if (waitingChatRoomList.isEmpty() && counsellingChatRoomList.isEmpty()) {
            return "counselor/list";
        } else if (!waitingChatRoomList.isEmpty() && counsellingChatRoomList.isEmpty()) {
            List<CounselorDto> waitingChatRoomDtoList = waitingChatRoomList.stream().map(c ->
                    new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname(), c.getChatRoomStatus(), c.getChatRoomReadStatus(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()))).collect(Collectors.toList());
            model.addAttribute("waitingChatRoomList", waitingChatRoomDtoList);
            return "counselor/list";
        } else if (waitingChatRoomList.isEmpty() && !counsellingChatRoomList.isEmpty()) {
            List<CounselorDto> counsellingChatRoomDtoList = counsellingChatRoomList.stream().map(c ->
                    new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname(), c.getChatRoomStatus(), c.getChatRoomReadStatus(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()))).collect(Collectors.toList());
            model.addAttribute("counsellingChatRoomList", counsellingChatRoomDtoList);
            return "counselor/list";
        }

        boolean hasNext = waitingChatRoomList.hasNext();

        List<CounselorDto> waitingChatRoomDtoList = waitingChatRoomList.stream()
                .map(c -> new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname(), c.getChatRoomStatus(), c.getChatRoomReadStatus(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()))).collect(Collectors.toList());
        List<CounselorDto> counsellingChatRoomDtoList = counsellingChatRoomList.stream()
                .map(c -> new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname(), c.getChatRoomStatus(), c.getChatRoomReadStatus(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()))).collect(Collectors.toList());

        model.addAttribute("host", appProperties.getHost());
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("waitingChatRoomList", waitingChatRoomDtoList);
        model.addAttribute("counsellingChatRoomList", counsellingChatRoomDtoList);
        return "counselor/list";
    }
    @GetMapping("/api/waiting-list")
    @ResponseBody
    public CounselorApiDto api_waitingList(@PageableDefault Pageable pageable){
        Slice<ChatRoom> findChatRoom = chatRoomRepository.findAllByCounselorIsNull(pageable);
        List<ChatRoom> chatRoomList = findChatRoom.getContent();
        List<CounselorDto> collect = chatRoomList.stream().map(c ->
                new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname(), c.getChatRoomStatus(), c.getChatRoomReadStatus(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(c.getRegDate()))).collect(Collectors.toList());
        return new CounselorApiDto(collect, findChatRoom.hasNext());
    }

    @GetMapping("/{sessionId}/join")
    public String counselor_join(@CurrentUser Counselor counselor, @PathVariable String sessionId, Model model, @PageableDefault Pageable pageable) {
        StatusNicknameDto statusNicknameDto = chatRoomService.changeStatus(sessionId, counselor);
        Slice<Chat> tempChatList = chatRepository.findChatRoomAccountCounselorFetchBySessionId(sessionId, pageable);
        String accountNickname = "";
        ChatRoomStatus previousStatus;
        if(tempChatList.hasContent()) {
            ChatRoom chatRoom = tempChatList.getContent().get(0).getChatRoom();
            accountNickname = chatRoom.getAccount().getNickname();
            previousStatus = statusNicknameDto.getPreviousStatus();
        }else{
            accountNickname = statusNicknameDto.getAccountNickname();
            previousStatus = ChatRoomStatus.WAITING;
        }
        String counselorNickname = counselor.getNickname();

        model.addAttribute("host", appProperties.getHost());
        model.addAttribute("hasNext", tempChatList.hasNext());
        model.addAttribute("accountNickname", accountNickname);
        model.addAttribute("counselorNickname", counselorNickname);
        model.addAttribute("chatList", tempChatList.getContent());
        model.addAttribute("previousStatus", previousStatus);
        model.addAttribute("sessionId", sessionId);
        model.addAttribute("hasNewMessage", false);

        return "counselor/join";
    }
    @GetMapping("/api/{sessionId}/read-chat")
    @ResponseBody
    public ResponseEntity api_readChat(@PathVariable String sessionId, @RequestParam(defaultValue = "Init", required = false) String role){
        if(role.equals("Init")){
            chatRoomService.enterSet("COUNSELOR", sessionId);
        }else {
            chatRoomService.changeRead(role, sessionId);
        }
        return ResponseEntity.ok().build();
    }

    @MessageMapping("/counselor/{sessionId}/join")
    @SendTo("/queue/chat/{sessionId}")
    public ChatDto counselor_counsel(@Payload ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);
        String message = "친절한 상담사 " + nickname + "입니다. 무엇을 도와드릴까요?";
        String sessionId = chatDto.getSessionId();
        List<ChatRoom> findChatRoom = chatRoomRepository.findAccountFetchBySessionId(sessionId);
        Long accountId = findChatRoom.get(0).getAccount().getId();
        String customerSocketURL = "/topic/customers/" + accountId + "/new-message";

        Chat newChat = chatService.addNewMessage(sessionId, message, ChatRole.COUNSELOR);
        chatDto = new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole(), sessionId);
        messagingTemplate.convertAndSend(customerSocketURL, chatDto);
        return chatDto;
    }

    @MessageMapping("/counselor/{sessionId}")
    @SendTo("/queue/chat/{sessionId}")
    public ChatDto counselor_chat(@Payload ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        String sessionId = chatDto.getSessionId();
        String email = accessor.getUser().getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);
        List<ChatRoom> findChatRoom = chatRoomRepository.findAccountFetchBySessionId(sessionId);
        Long accountId = findChatRoom.get(0).getAccount().getId();
        String customerSocketURL = "/topic/customers/" + accountId + "/new-message";

        Chat newChat = chatService.addNewMessage(sessionId, chatDto.getMessage(), ChatRole.COUNSELOR);
        chatDto = new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole(), sessionId);
        messagingTemplate.convertAndSend(customerSocketURL, chatDto);
        return chatDto;
    }

}
