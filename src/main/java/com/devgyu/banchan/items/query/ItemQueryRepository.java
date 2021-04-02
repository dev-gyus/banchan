package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.Item;

import java.util.List;

public interface ItemQueryRepository {
    List<Item> findAllByCategoryName(String categoryName);
    Item findItemOptionFetchById(Long itemId);

    Item findItemOptionCategoryFetchById(Long itemId);
}
