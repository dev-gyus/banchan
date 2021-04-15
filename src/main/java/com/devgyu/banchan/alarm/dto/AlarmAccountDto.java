package com.devgyu.banchan.alarm.dto;

import lombok.Data;

@Data
public class AlarmAccountDto {
    private String nickname;
    private String thumbnail;

    public AlarmAccountDto(String nickname, String thumbnail) {
        this.nickname = nickname;
        this.thumbnail = thumbnail;
    }
}
