package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.UserAccount;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class HttpHandShakeInterceptor implements HandshakeInterceptor {
    private final HttpServletRequest servletRequest;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        UserAccount userAccount = (UserAccount)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account account = userAccount.getAccount();
        String nickname = account.getNickname();
        String sessionId = servletRequest.getSession().getId();
        attributes.put(sessionId, nickname);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
