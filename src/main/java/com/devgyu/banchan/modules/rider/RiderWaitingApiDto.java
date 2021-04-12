package com.devgyu.banchan.modules.rider;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RiderWaitingApiDto {
    private List<Long> orderIdList = new ArrayList<>();
    private Map<Long, String> storeNicknameMap = new HashMap<>();
    private Map<Long, RiderAddressDto> storeAddressMap = new HashMap<>();
    private boolean last;

    // order-list page용
    public RiderWaitingApiDto(List<Long> orderIdList, Map<Long, String> storeNicknameMap, Map<Long, RiderAddressDto> storeAddressMap, boolean last) {
        this.orderIdList = orderIdList;
        this.storeNicknameMap = storeNicknameMap;
        this.storeAddressMap = storeAddressMap;
        this.last = last;
    }
}
