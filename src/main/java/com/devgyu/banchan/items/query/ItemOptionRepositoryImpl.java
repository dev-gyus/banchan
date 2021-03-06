package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.items.QItemOption;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.items.QItemOption.itemOption;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;

public class ItemOptionRepositoryImpl implements ItemOptionQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemOptionRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ItemOption> findAllByItemIdAndIdInStoreAuthTrue(Long itemId, List<Long> itemOptionIdList) {
        return queryFactory
                .selectFrom(itemOption)
                .distinct()
                .join(itemOption.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(item.id.eq(itemId).and(itemOption.id.in(itemOptionIdList)).and(storeOwner.managerAuthenticated.isTrue()))
                .fetch();
    }

    @Override
    public List<ItemOption> findAllByItemIdAndNamesIn(Long itemId, List<String> itemOptionNameList){
        return queryFactory
                .selectFrom(itemOption)
                .distinct()
                .join(itemOption.item, item).fetchJoin()
                .where(item.id.eq(itemId).and(itemOption.name.in(itemOptionNameList)))
                .fetch();
    }
    @Override
    public List<ItemOption> findAllStoreAuthTrueByItemIdAndNamesIn(Long itemId, List<String> itemOptionNameList) {
        return queryFactory
                .selectFrom(itemOption)
                .distinct()
                .join(itemOption.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(item.id.eq(itemId).and(itemOption.name.in(itemOptionNameList)).and(storeOwner.managerAuthenticated.isTrue()))
                .fetch();
    }

    @Override
    public List<ItemOption> findAllByItemIdAndIdIn(Long itemId, List<Long> itemOptionIdList){
        return queryFactory
                .selectFrom(itemOption)
                .distinct()
                .join(itemOption.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(item.id.eq(itemId).and(itemOption.id.in(itemOptionIdList)))
                .fetch();
    }


}
