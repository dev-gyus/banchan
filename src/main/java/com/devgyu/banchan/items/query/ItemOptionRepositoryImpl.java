package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.items.QItemOption;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.items.QItemOption.itemOption;

public class ItemOptionRepositoryImpl implements ItemOptionQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemOptionRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ItemOption> findAllByItemIdAndNamesIn(Long itemId, List<String> itemOptionNameList){
        return queryFactory
                .selectFrom(itemOption)
                .distinct()
                .join(itemOption.item, item).fetchJoin()
                .where(item.id.eq(itemId).and(itemOption.name.in(itemOptionNameList)))
                .fetch();
    }

}
