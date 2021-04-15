package com.devgyu.banchan.alarm.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AlarmApiDto {
    private List<AlarmDto> alarmDtoList = new ArrayList<>();
    private boolean last;

    public AlarmApiDto(List<AlarmDto> alarmDtoList, boolean last) {
        this.alarmDtoList = alarmDtoList;
        this.last = last;
    }
}
