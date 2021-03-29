package com.devgyu.banchan.account.dto;

import com.devgyu.banchan.account.Address;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MypageDto {
    private Long id;
    private String email;
    private String nickname;
    private String password;
    private String phone;
    private String name;
    private Address address;
    private String zipcode;
    private String road;
    private String jibun;
    private String detail;
    private String extra;

    public MypageDto(Long id, String email, String nickname, String password, String phone, String name, Address address) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.phone = phone;
        this.name = name;
        this.address = address;
    }
}
