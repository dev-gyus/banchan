package com.devgyu.banchan.modules.storeowner.query;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StoreOwnerQueryRepository {
    List<StoreOwner> findCategoriesFetchById(Long id);
    Page<StoreOwner> findCategoriesFetchByCategoryName(String categoryName, Pageable pageable);
}
