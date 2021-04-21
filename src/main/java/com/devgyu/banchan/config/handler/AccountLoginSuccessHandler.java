package com.devgyu.banchan.config.handler;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Transactional
public class AccountLoginSuccessHandler implements AuthenticationSuccessHandler {
    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private final AccountRepository accountRepository;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserAccount principal = (UserAccount) authentication.getPrincipal();
        Account account;
        if(principal.getCustomer() != null){
            account = principal.getCustomer();
        }else if(principal.getStoreOwner() != null){
            account = principal.getStoreOwner();
        }else if(principal.getRider() != null){
            account = principal.getRider();
        }else if(principal.getAdmin() != null){
            account = principal.getAdmin();
        }else if(principal.getCounselor() != null){
            account = principal.getCounselor();
        }else{
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        Account findAccount = accountRepository.findById(account.getId()).get();
        if(findAccount.getFailCount() >= 5){
            throw new LockedException("계정이 잠겼습니다. 비밀번호 찾기 후 로그인 해 주세요");
        }else if(findAccount.isBlocked()){
            throw new LockedException("계정이 잠겼습니다. 정지사유:" + findAccount.getBlockReason());
        } else{
            findAccount.setFailCount(0);
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        // 인증이 필요한 리소스에 접근하려다 로그인 화면으로 넘어간경우
        if(savedRequest != null){
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }else{  // 직접 로그인 페이지로 이동해서 들어온경우 메인페이지로 리다이렉트
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}
