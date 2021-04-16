package com.devgyu.banchan.alarm.dto;

import lombok.Data;

@Data
public class AlarmAccountDto {
    private String name;
    private String thumbnail;

    public AlarmAccountDto(String name, String thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }
}
