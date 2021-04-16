package com.devgyu.banchan.admin.dto;

import lombok.Data;

@Data
public class AdminRiderDto {
    private Long id;
    private String nickname;
    private String name;
    private String driverLicense;
    private String road;

    public AdminRiderDto(Long id, String nickname, String name, String road, String driverLicense) {
        this.id = id;
        this.nickname = nickname;
        this.name = name;
        this.road = road;
        this.driverLicense = driverLicense;
    }
}
