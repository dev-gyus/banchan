package com.devgyu.banchan.modules.rider.query;

import com.devgyu.banchan.modules.rider.RiderOrders;
import com.devgyu.banchan.orders.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RiderOrdersQueryRepository {
    public Page<RiderOrders> findAccountOrdersItemStoreFetchByRiderIdAndStatus(Long riderId, OrderStatus orderStatus, Pageable pageable);
    public List<RiderOrders> findAccountOrdersItemStoreFetchByOrdersIdAndRiderId(Long ordersId, Long riderId);
}
