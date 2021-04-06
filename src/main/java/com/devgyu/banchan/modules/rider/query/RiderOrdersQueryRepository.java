package com.devgyu.banchan.modules.rider.query;

import com.devgyu.banchan.modules.rider.RiderOrders;
import com.devgyu.banchan.orders.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RiderOrdersQueryRepository {
    public Page<RiderOrders> findOrdersItemStoreFetchByRiderIdAndStatus(Long riderId, OrderStatus orderStatus, Pageable pageable);
}
