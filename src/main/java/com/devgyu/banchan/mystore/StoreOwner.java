package com.devgyu.banchan.mystore;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreOwner extends Account {

    private String tel;

    public StoreOwner(String email, String nickname, String password, String name, String phone, Address address, String tel) {
        super(email, nickname, password, name, phone, Roles.ROLE_OWNER, address, UUID.randomUUID().toString());
        this.tel = tel;
    }
}
