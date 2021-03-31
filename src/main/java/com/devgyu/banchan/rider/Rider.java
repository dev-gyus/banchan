package com.devgyu.banchan.rider;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import lombok.*;

import javax.persistence.*;

@Entity
@DiscriminatorColumn
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Rider extends Account {

    public Rider(String email, String nickname, String password, String name, String phone, Address address, String emailToken) {
        super(email, nickname, password, name, phone, Roles.ROLE_RIDER, address, emailToken);
    }
}
