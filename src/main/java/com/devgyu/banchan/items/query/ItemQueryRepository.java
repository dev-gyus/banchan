package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.Item;

import java.util.List;

public interface ItemQueryRepository {
    List<Item> findAllByCategoryName(String categoryName);
}
