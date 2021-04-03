package com.devgyu.banchan.ordersitem;

import com.devgyu.banchan.ordersitem.query.OrdersItemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersItemRepository extends JpaRepository<OrdersItem, Long>, OrdersItemQueryRepository {
}
