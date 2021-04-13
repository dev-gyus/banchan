package com.devgyu.banchan.modules.storecategory;

import com.devgyu.banchan.modules.storecategory.query.StoreCategoryQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long>, StoreCategoryQueryRepository {
}
