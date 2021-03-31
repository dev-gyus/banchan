package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.account.UserAccount;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MypageInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
        }else if(authentication instanceof UsernamePasswordAuthenticationToken) {
            UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(userAccount.getRole() == Roles.ROLE_OWNER || userAccount.getRole() == Roles.ROLE_RIDER){
                throw new IllegalAccessException("잘못된 요청입니다.");
            }
        }
        return true;
    }
}
