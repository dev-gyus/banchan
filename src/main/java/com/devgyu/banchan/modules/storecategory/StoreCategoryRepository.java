package com.devgyu.banchan.modules.storecategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
}
