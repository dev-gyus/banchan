package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.ItemOption;

import java.util.List;

public interface ItemOptionQueryRepository {
    List<ItemOption> findAllByItemIdAndNamesIn(Long itemId, List<String> itemOptionNameList);

    List<ItemOption> findAllByItemIdAndIdIn(Long itemId, List<Long> itemOptionIdList);
}
