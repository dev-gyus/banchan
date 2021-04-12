package com.devgyu.banchan.mystore;

import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.ordersitem.OrdersItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MystoreApiDto {
    private List<MystoreOrdersDto> mystoreOrdersDtoList = new ArrayList<>();
    private Map<Long, MystoreAccountDto> mystoreAccountDtoMap = new HashMap<>();
    private Map<Long, List<MystoreOrderItemDto>> mystoreOrderItemDtoMap = new HashMap<>();
    private Map<LocalDateTime, List<MystoreItemOptionDto>> mystoreItemOptionDtoMap = new HashMap<>();
    private boolean last;

    public MystoreApiDto(List<MystoreOrdersDto> mystoreOrdersDtoList, Map<Long, MystoreAccountDto> mystoreAccountDtoMap, Map<Long, List<MystoreOrderItemDto>> mystoreOrderItemDtoMap, Map<LocalDateTime, List<MystoreItemOptionDto>> mystoreItemOptionDtoMap, boolean last) {
        this.mystoreOrdersDtoList = mystoreOrdersDtoList;
        this.mystoreAccountDtoMap = mystoreAccountDtoMap;
        this.mystoreOrderItemDtoMap = mystoreOrderItemDtoMap;
        this.mystoreItemOptionDtoMap = mystoreItemOptionDtoMap;
        this.last = last;
    }
}
