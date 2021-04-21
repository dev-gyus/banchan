package com.devgyu.banchan.modules.counselor;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Counselor extends Account {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "counselor")
    @JsonIgnore
    private List<ChatRoom> counselList = new ArrayList<>();

    public Counselor(String email, String nickname, String password, String name, String phone, Roles role, Address address) {
        super(email, nickname, password, name, phone, role, address, UUID.randomUUID().toString());
    }
}
