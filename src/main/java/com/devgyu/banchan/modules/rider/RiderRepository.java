package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    Rider findByEmail(String email);
}
