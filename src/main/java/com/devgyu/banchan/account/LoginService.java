package com.devgyu.banchan.account;

import com.devgyu.banchan.admin.Admin;
import com.devgyu.banchan.modules.counselor.Counselor;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.rider.Rider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account findAccount = accountRepository.findByEmail(username);
        if (findAccount == null) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }
        if(findAccount instanceof com.devgyu.banchan.account.customer.Customer){
            return new UserAccount(findAccount.getEmail(), findAccount.getPassword(), Arrays.asList(new SimpleGrantedAuthority(findAccount.getRole().toString())), findAccount.getId(), (com.devgyu.banchan.account.customer.Customer) findAccount);
        }else if(findAccount instanceof StoreOwner){
            return new UserAccount(findAccount.getEmail(), findAccount.getPassword(), Arrays.asList(new SimpleGrantedAuthority(findAccount.getRole().toString())), findAccount.getId(), (StoreOwner) findAccount);
        }else if(findAccount instanceof Rider){
            return new UserAccount(findAccount.getEmail(), findAccount.getPassword(), Arrays.asList(new SimpleGrantedAuthority(findAccount.getRole().toString())), findAccount.getId(), (Rider) findAccount);
        }else if(findAccount instanceof Admin){
            return new UserAccount(findAccount.getEmail(), findAccount.getPassword(), Arrays.asList(new SimpleGrantedAuthority(findAccount.getRole().toString())), findAccount.getId(), (Admin) findAccount);
        }else if(findAccount instanceof Counselor){
            return new UserAccount(findAccount.getEmail(), findAccount.getPassword(), Arrays.asList(new SimpleGrantedAuthority(findAccount.getRole().toString())), findAccount.getId(), (Counselor) findAccount);
        }
        return null;
    }
}
