package com.devgyu.banchan.config.handler;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Transactional
public class AccountLoginFailureHandler implements AuthenticationFailureHandler {
    private final AccountRepository accountRepository;
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        if(exception instanceof BadCredentialsException) { // 그냥 아이디, 비밀번호가 일치하지 않아서 진입했을경우
            String email = request.getParameter("email");

            Account findAccount = accountRepository.findByEmail(email);
            if (findAccount == null) {
                throw new IllegalArgumentException("잘못된 요청입니다.");
            }

            if (findAccount.getFailCount() >= 5) {
                request.setAttribute("isLocked", true);
                request.setAttribute("exceptionMessage", "계정이 잠겼습니다. 비밀번호 찾기 후 로그인 해 주세요");
            } else {
                findAccount.addFailCount();
                request.setAttribute("isFailed", true);
            }
        }else if(exception instanceof LockedException){ // LoginSuccessHandler에서 잠김확인시 넘어옴
            request.setAttribute("isLocked", true);
            request.setAttribute("exceptionMessage", exception.getMessage());
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/login");
        requestDispatcher.forward(request, response);
    }
}
