package com.devgyu.banchan.modules.counselor;

import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.devgyu.banchan.account.chatroom.ChatRoomRepository;
import com.devgyu.banchan.account.chatroom.ChatRoomService;
import com.devgyu.banchan.account.chatroom.ChatRoomStatus;
import com.devgyu.banchan.chat.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
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

    @GetMapping("/list")
    public String counselor_list(@CurrentUser Counselor counselor, @PageableDefault Pageable pageable, Model model) {
        Page<ChatRoom> waitingChatRoomList = chatRoomRepository.findAllByWaiting(counselor.getId(), pageable);
        List<ChatRoom> counsellingChatRoomList = chatRoomRepository.findAllByCounsellingAndCounselorId(counselor.getId());

        if (waitingChatRoomList.isEmpty() && counsellingChatRoomList.isEmpty()) {
            return "counselor/list";
        } else if (!waitingChatRoomList.isEmpty() && counsellingChatRoomList.isEmpty()) {
            List<CounselorDto> waitingChatRoomDtoList = waitingChatRoomList.stream().map(c -> new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname())).collect(Collectors.toList());
            model.addAttribute("waitingChatRoomList", waitingChatRoomDtoList);
            return "counselor/list";
        } else if (waitingChatRoomList.isEmpty() && !counsellingChatRoomList.isEmpty()) {
            List<CounselorDto> counsellingChatRoomDtoList = counsellingChatRoomList.stream().map(c -> new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname())).collect(Collectors.toList());
            model.addAttribute("counsellingChatRoomList", counsellingChatRoomDtoList);
            return "counselor/list";
        }

        boolean hasNext = waitingChatRoomList.hasNext();

        List<CounselorDto> waitingChatRoomDtoList = waitingChatRoomList.stream()
                .map(c -> new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname())).collect(Collectors.toList());
        List<CounselorDto> counsellingChatRoomDtoList = counsellingChatRoomList.stream()
                .map(c -> new CounselorDto(c.getSessionId(), c.getId(), c.getAccount().getNickname())).collect(Collectors.toList());

        model.addAttribute("hasNext", hasNext);
        model.addAttribute("waitingChatRoomList", waitingChatRoomDtoList);
        model.addAttribute("counsellingChatRoomList", counsellingChatRoomDtoList);
        return "counselor/list";
    }

    @GetMapping("/{sessionId}/join")
    public String counselor_join(@CurrentUser Counselor counselor, @PathVariable String sessionId, Model model, @PageableDefault Pageable pageable) {
        ChatRoomStatus previousStatus = chatRoomService.changeStatus(sessionId, counselor);
        Slice<Chat> tempChatList = chatRepository.findChatRoomAccountCounselorFetchBySessionId(sessionId, pageable);
        ChatRoom chatRoom = tempChatList.getContent().get(0).getChatRoom();
        String accountNickname = chatRoom.getAccount().getNickname();
        String counselorNickname = chatRoom.getCounselor().getNickname();

        model.addAttribute("hasNext", tempChatList.hasNext());
        model.addAttribute("accountNickname", accountNickname);
        model.addAttribute("counselorNickname", counselorNickname);
        model.addAttribute("chatList", tempChatList.getContent());
        model.addAttribute("previousStatus", previousStatus);
        model.addAttribute("sessionId", sessionId);
        return "counselor/join";
    }

    @MessageMapping("/counselor/{sessionId}/join")
    @SendTo("/queue/chat/{sessionId}")
    public ChatDto counselor_counsel(@Payload ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        String email = accessor.getUser().getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);
        String message = "친절한 상담사 " + nickname + "입니다. 무엇을 도와드릴까요?";

        Chat newChat = chatService.addNewMessage(chatDto.getSessionId(), message, ChatRole.COUNSELOR);
        chatDto = new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole());
        return chatDto;
    }

    @MessageMapping("/counselor/{sessionId}")
    @SendTo("/queue/chat/{sessionId}")
    public ChatDto counselor_chat(@Payload ChatDto chatDto, SimpMessageHeaderAccessor accessor) {
        String sessionId = chatDto.getSessionId();
        String email = accessor.getUser().getName();
        String nickname = (String) accessor.getSessionAttributes().get(email);

        Chat newChat = chatService.addNewMessage(sessionId, chatDto.getMessage(), ChatRole.COUNSELOR);
        chatDto = new ChatDto(nickname, newChat.getMessage(), DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(newChat.getSendDate()), newChat.getChatRole());
        return chatDto;
    }

}
