package com.devgyu.banchan.modules.rider;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class RiderWaitingApiDto {
    private List<RiderOrderDto> riderOrderDtoList = new ArrayList<>();
    private boolean last;

    public RiderWaitingApiDto(List<RiderOrderDto> riderOrderDtoList, boolean last) {
        this.riderOrderDtoList = riderOrderDtoList;
        this.last = last;
    }
}
