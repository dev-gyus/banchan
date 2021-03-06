package com.devgyu.banchan.orders;

import com.devgyu.banchan.orders.query.OrdersQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersQueryRepository {
}
