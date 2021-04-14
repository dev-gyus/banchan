package com.devgyu.banchan.orders;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersApiDto {
    private List<CompOrdersDto> compOrdersDtoList = new ArrayList<>();
    private List<NotCompOrdersDto> notCompOrdersDtoList = new ArrayList<>();
    private boolean last;


    public OrdersApiDto(List<CompOrdersDto> compOrdersDtoList, boolean last) {
        this.compOrdersDtoList = compOrdersDtoList;
        this.last = last;
    }
}
