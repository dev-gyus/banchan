package com.devgyu.banchan.ordersitem.query;

import com.devgyu.banchan.orders.NotCompOrderItemDto;
import com.devgyu.banchan.orders.OrdersItemOptionDto;
import com.devgyu.banchan.ordersitem.OrdersItem;

import java.util.List;

public interface OrdersItemQueryRepository {

    List<OrdersItem> findItemOptionFetchByIdIn(List<Long> orderItemIdList);

    List<OrdersItem> findOrdersItemItemOptionStoreOwnerByOrderId(Long orderId);

    List<NotCompOrderItemDto> findOrdersItemItemOptionStoreOwnerByOrderIdIn(List<Long> ordersIdList);
}
