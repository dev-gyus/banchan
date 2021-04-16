package com.devgyu.banchan.modules.rider.query;

import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderOrderDto;
import com.devgyu.banchan.modules.rider.RiderOrders;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RiderQueryRepository {

    Page<RiderOrderDto> findAccountItemFetchByStoreRoadIdAndStatus(String doSigungu, OrderStatus orderStatus, Pageable pageable);

    Page<RiderOrderDto> findAccountItemFetchByRiderIdAndStatus(Long id, OrderStatus orderStatus, Pageable pageable);

    Page<Rider> findAllByManagerAuthenticatedAndSigungu(boolean managerAuth, String sigungu, Pageable pageable);
}
