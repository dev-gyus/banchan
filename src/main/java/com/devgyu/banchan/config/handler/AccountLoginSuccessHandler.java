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
            throw new IllegalArgumentException("????????? ???????????????.");
        }

        Account findAccount = accountRepository.findById(account.getId()).get();
        if(findAccount.getFailCount() >= 5){
            throw new LockedException("????????? ???????????????. ???????????? ?????? ??? ????????? ??? ?????????");
        }else if(findAccount.isBlocked()){
            throw new LockedException("????????? ???????????????. ????????????:" + findAccount.getBlockReason());
        } else{
            findAccount.setFailCount(0);
        }

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        // ????????? ????????? ???????????? ??????????????? ????????? ???????????? ???????????????
        if(savedRequest != null){
            redirectStrategy.sendRedirect(request, response, savedRequest.getRedirectUrl());
        }else{  // ?????? ????????? ???????????? ???????????? ??????????????? ?????????????????? ???????????????
            redirectStrategy.sendRedirect(request, response, "/");
        }
    }
}
