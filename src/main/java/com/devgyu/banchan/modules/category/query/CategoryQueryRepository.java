package com.devgyu.banchan.modules.category.query;

import com.devgyu.banchan.modules.category.Category;

import java.util.List;

public interface CategoryQueryRepository {
    List<Category> findAllByStoreOwnerId(Long storeOwnerId);
}
