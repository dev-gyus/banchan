package com.devgyu.banchan.admin;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;

@Entity
@Getter @Setter
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin extends Account {
    public Admin(String email, String nickname, String password, String name, String phone, Roles role, Address address, String emailToken) {
        super(email, nickname, password, name, phone, role, address, emailToken);
    }
}
