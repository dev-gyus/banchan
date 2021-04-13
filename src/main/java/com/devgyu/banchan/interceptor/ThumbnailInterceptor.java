package com.devgyu.banchan.interceptor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.account.customer.CustomerRepository;
import com.devgyu.banchan.admin.Admin;
import com.devgyu.banchan.admin.AdminRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class ThumbnailInterceptor implements HandlerInterceptor {
    private final CustomerRepository customerRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RiderRepository riderRepository;
    private final AdminRepository adminRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
        } else if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UserAccount userAccount = (UserAccount) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long id = 0L;
            if (userAccount.getCustomer() != null) {
                id = userAccount.getCustomer().getId();
                Customer account = customerRepository.findById(id).get();

                //쿼리 다이어트용. 매 페이지 이동시마다 인터셉터에서 로그인한 유저엔티티 최신상태로 유지해줌
                refreshAuthentication(userAccount, account);

                request.setAttribute("navAccount", account);
            } else if (userAccount.getStoreOwner() != null) {
                id = userAccount.getStoreOwner().getId();
                StoreOwner account = storeOwnerRepository.findById(id).get();

                //쿼리 다이어트용. 매 페이지 이동시마다 인터셉터에서 로그인한 유저엔티티 최신상태로 유지해줌
                refreshAuthentication(userAccount, account);

                request.setAttribute("navAccount", account);
            } else if (userAccount.getRider() != null) {
                id = userAccount.getRider().getId();
                Rider account = riderRepository.findById(id).get();

                //쿼리 다이어트용. 매 페이지 이동시마다 인터셉터에서 로그인한 유저엔티티 최신상태로 유지해줌
                refreshAuthentication(userAccount, account);

                request.setAttribute("navAccount", account);
            } else if (userAccount.getAdmin() != null) {
                id = userAccount.getId();
                Admin account = adminRepository.findById(id).get();

                //쿼리 다이어트용. 매 페이지 이동시마다 인터셉터에서 로그인한 유저엔티티 최신상태로 유지해줌
                refreshAuthentication(userAccount, account);
                request.setAttribute("navAccount", account);
            }
        }
        return true;
    }

    private void refreshAuthentication(UserAccount userAccount, Account account) {
        UserAccount newUserAccount;
        if (account instanceof Customer) {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Customer) account);
        } else if (account instanceof StoreOwner) {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (StoreOwner) account);
        } else if (account instanceof Rider) {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Rider) account);
        } else {
            newUserAccount = new UserAccount(account.getEmail(), account.getPassword(),
                    Arrays.asList(new SimpleGrantedAuthority(account.getRole().toString())), account.getId(), (Admin) account);
        }
        Authentication newAuth = new UsernamePasswordAuthenticationToken(newUserAccount, account.getPassword(), userAccount.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
