package com.devgyu.banchan.ordersitem.query;

import com.devgyu.banchan.ordersitem.OrdersItem;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersItemQueryRepository {

    List<OrdersItem> findItemOptionFetchByIdIn(List<Long> orderItemIdList);

    List<OrdersItem> findOrdersItemItemOptionStoreOwnerByOrderId(Long orderId);
}
