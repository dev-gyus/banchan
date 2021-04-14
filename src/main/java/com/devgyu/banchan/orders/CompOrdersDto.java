package com.devgyu.banchan.orders;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class CompOrdersDto {
    private String orderDate;
    private String storeName;
    private boolean reviewed;
    private Long ordersId;
    private Map<Long, List<CompOrderItemDto>> compOrderItemDtoMap = new HashMap<>();

    public CompOrdersDto(String orderDate, String storeName, boolean reviewed, Long ordersId, Map<Long, List<CompOrderItemDto>> compOrderItemDtoMap) {
        this.orderDate = orderDate;
        this.storeName = storeName;
        this.reviewed = reviewed;
        this.ordersId = ordersId;
        this.compOrderItemDtoMap = compOrderItemDtoMap;
    }
}
