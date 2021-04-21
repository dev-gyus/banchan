package com.devgyu.banchan.account;

import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.admin.Admin;
import com.devgyu.banchan.modules.counselor.Counselor;
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
    private Counselor counselor;
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
    public UserAccount(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, Counselor counselor) {
        super(username, password, authorities);
        this.id = id;
        this.counselor = counselor;
        this.role = counselor.getRole();
    }

    public Account getAccount(){
        if(this.customer != null){
            return this.customer;
        }else if(this.storeOwner != null){
            return this.storeOwner;
        }else if(this.rider != null){
            return this.rider;
        }else if(this.counselor != null){
            return this.counselor;
        }else {
            return this.admin;
        }
    }
}
