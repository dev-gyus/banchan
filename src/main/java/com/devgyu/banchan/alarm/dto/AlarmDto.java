package com.devgyu.banchan.alarm.dto;

import com.devgyu.banchan.alarm.AlarmType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AlarmDto {
    private String alertDate;
    private String content;
    private AlarmType alarmType;

    public AlarmDto(String alertDate, String content, AlarmType alarmType) {
        this.alertDate = alertDate;
        this.content = content;
        this.alarmType = alarmType;
    }
}
