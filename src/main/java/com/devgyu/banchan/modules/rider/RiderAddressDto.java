package com.devgyu.banchan.modules.rider;

import lombok.Data;

@Data
public class RiderAddressDto {
    private String road;
    private String detail;

    public RiderAddressDto(String road, String detail) {
        this.road = road;
        this.detail = detail;
    }
}
