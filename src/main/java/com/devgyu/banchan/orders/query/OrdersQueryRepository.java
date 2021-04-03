package com.devgyu.banchan.orders.query;

import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.List;

public interface OrdersQueryRepository {
    Page<Orders> findAccountItemFetchByIdAndStatus(Long accountId, Pageable pageable, OrderStatus firstCondition, @Nullable OrderStatus secondCondition);
}