package com.devgyu.banchan.mystore;

import com.devgyu.banchan.account.Address;
import lombok.Data;

@Data
public class MystoreAccountDto {
    private String jibun;
    private String road;
    private String detail;
    private String extra;
    private String nickname;

    public MystoreAccountDto(String jibun, String road, String detail, String extra, String nickname) {
        this.jibun = jibun;
        this.road = road;
        this.detail = detail;
        this.extra = extra;
        this.nickname = nickname;
    }
}
