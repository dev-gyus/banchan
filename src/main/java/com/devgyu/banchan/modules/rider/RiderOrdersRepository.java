package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.modules.rider.RiderOrders;
import com.devgyu.banchan.modules.rider.query.RiderOrdersQueryRepository;
import com.devgyu.banchan.orders.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderOrdersRepository extends JpaRepository<RiderOrders, Long>, RiderOrdersQueryRepository {

}
