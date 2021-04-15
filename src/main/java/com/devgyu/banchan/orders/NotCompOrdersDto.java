package com.devgyu.banchan.orders;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class NotCompOrdersDto {
    private String orderDate;
    private String storeName;
    private Long ordersId;
    private Map<Long, List<NotCompOrderItemDto>> compOrderItemDtoMap = new HashMap<>();
    private int orderPrice;

    public NotCompOrdersDto(String orderDate, String storeName, Long ordersId, Map<Long, List<NotCompOrderItemDto>> compOrderItemDtoMap, int orderPrice) {
        this.orderDate = orderDate;
        this.storeName = storeName;
        this.ordersId = ordersId;
        this.compOrderItemDtoMap = compOrderItemDtoMap;
        this.orderPrice = orderPrice;
    }
}
