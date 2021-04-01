package com.devgyu.banchan.modules.storeowner.query;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface StoreOwnerQueryRepository {
    List<StoreOwner> findCategoriesFetchById(Long id);
}
