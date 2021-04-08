package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.orders.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    Rider findByEmail(String email);


}
