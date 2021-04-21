package com.devgyu.banchan.chat;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.devgyu.banchan.account.chatroom.ChatRoomService;
import com.devgyu.banchan.account.chatroom.ChatRoomStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

import static com.devgyu.banchan.account.Roles.ROLE_ADMIN;
import static com.devgyu.banchan.account.Roles.ROLE_COUNSELOR;

@Configuration
@RequiredArgsConstructor
public class HttpHandShakeInterceptor implements HandshakeInterceptor {
    private final ChatRoomService chatRoomService;
    private final AccountRepository accountRepository;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = userAccount.getAccount();
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession session = servletRequest.getServletRequest().getSession();
        String sessionId = session.getId();

        String nickname = account.getNickname();
        String username = account.getEmail();
        attributes.put(username, nickname);
        attributes.put("sessionId", sessionId);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
