package com.devgyu.banchan.modules.rider;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RiderNotWaitingApiDto {
    List<RiderOrderDto> riderOrderDtoList = new ArrayList<>();
    private boolean last;

    public RiderNotWaitingApiDto(List<RiderOrderDto> riderOrderDtoList, boolean last) {
        this.riderOrderDtoList = riderOrderDtoList;
        this.last = last;
    }
}
