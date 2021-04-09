package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Rider, StoreOwner 계정은 처음 가입시, 회원정보 수정시(StoreOwner는 가게상품 추가/삭제 시) 관리자의 승인을 받아야함
 * Rider, StoreOwner 정보 수정했는데 url접근, 즐겨찾기등으로 접근한 경우 판단하기위한 인터셉터
 */
@Component
@RequiredArgsConstructor
public class RiderAuthInterceptor implements HandlerInterceptor {
    private final RiderRepository riderRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userAccount.getCustomer() != null){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }else if(userAccount.getStoreOwner() != null){
            throw new IllegalArgumentException("잘못된 요청입니다");
        }else{
            Rider rider = riderRepository.findById(userAccount.getRider().getId()).get();
            if(!rider.isManagerAuthenticated()){
                response.sendRedirect("/rider/auth-waiting");
                return false;
            }
        }
        return true;
    }
}
