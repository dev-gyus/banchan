package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class ThumbnailInterceptor implements HandlerInterceptor {
    private final AccountRepository accountRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
        }else if(authentication instanceof UsernamePasswordAuthenticationToken) {
            UserAccount principal = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long id = principal.getAccount().getId();
            Account account = accountRepository.findById(id).get();
            request.setAttribute("navAccount", account);
        }
        return true;
    }
}
