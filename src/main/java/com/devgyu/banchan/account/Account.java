package com.devgyu.banchan.account;

import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.orders.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public abstract class Account {
    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    private String password;
    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Embedded
    private Address address;

    private String emailToken;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String thumbnail;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Orders> ordersList = new ArrayList<>();

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Cart cart;

    public Account(String email, String nickname, String password, String name, String phone, Roles role, Address address, String emailToken) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.address = address;
        this.emailToken = emailToken;
    }
}
