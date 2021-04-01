package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.account.customer.CustomerRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.rider.Rider;
import com.devgyu.banchan.rider.RiderRepository;
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
    private final CustomerRepository customerRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RiderRepository riderRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof AnonymousAuthenticationToken){
        }else if(authentication instanceof UsernamePasswordAuthenticationToken) {
            UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long id = null;
            if(userAccount.getCustomer() != null){
                id = userAccount.getCustomer().getId();
                Customer account = customerRepository.findById(id).get();
                request.setAttribute("navAccount", account);
            }else if(userAccount.getStoreOwner() != null){
                id = userAccount.getStoreOwner().getId();
                StoreOwner account = storeOwnerRepository.findById(id).get();
                request.setAttribute("navAccount", account);
            }else if(userAccount.getRider() != null){
                id = userAccount.getRider().getId();
                Rider account = riderRepository.findById(id).get();
                request.setAttribute("navAccount", account);
            }
        }
        return true;
    }
}
