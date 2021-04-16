package com.devgyu.banchan.admin.dto;

import lombok.Data;

@Data
public class AdminStoreDto {
    private Long id;
    private String nickname;
    private String road;
    private String tel;

    public AdminStoreDto(Long id, String nickname, String road, String tel) {
        this.id = id;
        this.nickname = nickname;
        this.road = road;
        this.tel = tel;
    }
}
