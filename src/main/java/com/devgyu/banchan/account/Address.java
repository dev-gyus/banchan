package com.devgyu.banchan.account;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Address {
    private String zipcode;
    private String road;
    private String jibun;
    private String detail;
    private String extra;

    public Address(String zipcode, String road, String jibun, String detail, String extra) {
        this.zipcode = zipcode;
        this.road = road;
        this.jibun = jibun;
        this.detail = detail;
        this.extra = extra;
    }
}
