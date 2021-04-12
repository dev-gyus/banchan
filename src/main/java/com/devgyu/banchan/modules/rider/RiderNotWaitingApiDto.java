package com.devgyu.banchan.modules.rider;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RiderNotWaitingApiDto {
    private Map<LocalDateTime, String> storeNicknameMap = new HashMap<>();
    private Map<LocalDateTime, RiderAddressDto> storeAddressMap = new HashMap<>();
    private List<LocalDateTime> orderRegDateList = new ArrayList<>();
    private Map<LocalDateTime, RiderAddressDto> accountAddressMap = new HashMap<>();
    private Map<LocalDateTime, Long> orderIdMap = new HashMap<>();
    private boolean last;

    // orderStatus = delivery_ready || delivery_start 일경우
    public RiderNotWaitingApiDto(Map<LocalDateTime, String> storeNicknameMap, Map<LocalDateTime, RiderAddressDto> storeAddressMap, List<LocalDateTime> orderRegDateList, Map<LocalDateTime, RiderAddressDto> accountAddressMap, Map<LocalDateTime, Long> orderIdMap, boolean last) {
        this.storeNicknameMap = storeNicknameMap;
        this.storeAddressMap = storeAddressMap;
        this.orderRegDateList = orderRegDateList;
        this.accountAddressMap = accountAddressMap;
        this.orderIdMap = orderIdMap;
        this.last = last;
    }

    // orderStatus = delivery_completed 일경우

    public RiderNotWaitingApiDto(Map<LocalDateTime, String> storeNicknameMap, Map<LocalDateTime, RiderAddressDto> storeAddressMap, List<LocalDateTime> orderRegDateList, boolean last) {
        this.storeNicknameMap = storeNicknameMap;
        this.storeAddressMap = storeAddressMap;
        this.orderRegDateList = orderRegDateList;
        this.last = last;
    }
}
