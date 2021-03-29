package com.devgyu.banchan.account;

import com.devgyu.banchan.account.Account;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter @Setter
@EqualsAndHashCode(of = "account")
public class UserAccount extends User {
    private Account account;

    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Account account) {
        super(username, password, authorities);
        this.account = account;
    }
}
