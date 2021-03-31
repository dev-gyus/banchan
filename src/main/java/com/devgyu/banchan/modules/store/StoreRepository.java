package com.devgyu.banchan.modules.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StoreRepository extends JpaRepository<Store, Long> {
}
