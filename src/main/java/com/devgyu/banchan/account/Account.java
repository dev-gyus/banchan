package com.devgyu.banchan.account;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Account {
    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Lob @Basic(fetch = FetchType.EAGER)
    private String thumbnail;

    private String password;
    private String name;
    private String phone;

    @Enumerated
    private Roles role;

    @Embedded
    private Address address;

    private String emailToken;

}
