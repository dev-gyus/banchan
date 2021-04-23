package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.chatroom.ChatRoom;
import com.devgyu.banchan.chatroom.ChatRoomRepository;
import com.devgyu.banchan.chatroom.ChatRoomService;
import com.devgyu.banchan.modules.counselor.Counselor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoomEnterInterceptor implements ChannelInterceptor {
    private final ChatRoomService chatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        String sessionId = accessor.getSessionId();
        String destination = accessor.getDestination();

        String roomSessionId = "";

        if(destination != null && destination.length() > 13) {
            String substring = destination.substring(0, 12);
            if (substring.equals("/queue/chat/")) {
                roomSessionId = destination.split(substring)[1];
            }
        }
        if(!roomSessionId.equals("") && command == StompCommand.SUBSCRIBE){
            accessor.getSessionAttributes().put(sessionId, roomSessionId);
            accessor.getSessionAttributes().put("isChatJoined", true);
        }

        if(accessor.getSessionAttributes().get("isChatJoined") != null && command == StompCommand.DISCONNECT){
            String targetRoomSession = (String) accessor.getSessionAttributes().get(sessionId);

            Authentication authentication = (UsernamePasswordAuthenticationToken) accessor.getHeader("simpUser");
            UserAccount userAccount = (UserAccount) authentication.getPrincipal();
            Account account = userAccount.getAccount();

            chatRoomService.accountExit(account, targetRoomSession);
            accessor.getSessionAttributes().remove("isChatJoined");
        }
        return message;
    }
}
