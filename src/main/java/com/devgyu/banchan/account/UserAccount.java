package com.devgyu.banchan.account;

import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.admin.Admin;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.rider.Rider;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter @Setter
@EqualsAndHashCode(of = "id", callSuper = false)
public class UserAccount extends User {
    private Long id;
    private Customer customer;
    private StoreOwner storeOwner;
    private Rider rider;
    private Admin admin;
    private Roles role;

    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Customer customer) {
        super(username, password, authorities);
        this.id = id;
        this.customer = customer;
        this.role = customer.getRole();
    }
    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, StoreOwner storeOwner) {
        super(username, password, authorities);
        this.id = id;
        this.storeOwner = storeOwner;
        this.role = storeOwner.getRole();
    }
    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Rider rider) {
        super(username, password, authorities);
        this.id = id;
        this.rider = rider;
        this.role = rider.getRole();
    }

    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Admin admin) {
        super(username, password, authorities);
        this.id = id;
        this.admin = admin;
        this.role = admin.getRole();
    }
}
