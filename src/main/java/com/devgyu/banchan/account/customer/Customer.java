package com.devgyu.banchan.account.customer;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.cart.Cart;
import lombok.*;

import javax.persistence.*;

@Entity
@DiscriminatorColumn
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends Account {

    public Customer(String email, String nickname, String password, String name, String phone, Address address, String emailToken) {
        super(email, nickname, password, name, phone, Roles.ROLE_USER, address, emailToken);
    }
}
