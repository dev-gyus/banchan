package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemStatus;
import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.items.QItemOption;
import com.devgyu.banchan.modules.category.QCategory;
import com.devgyu.banchan.modules.storecategory.QStoreCategory;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.items.QItemOption.itemOption;
import static com.devgyu.banchan.modules.category.QCategory.category;
import static com.devgyu.banchan.modules.storecategory.QStoreCategory.storeCategory;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;

public class ItemRepositoryImpl implements ItemQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Item> findAllByCategoryAndStore(String categoryName, Long storeId){
        return queryFactory
                .selectFrom(item)
                .join(item.category, category).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(category.name.eq(categoryName).and(storeOwner.id.eq(storeId)).and(item.itemStatus.eq(ItemStatus.NORMAL)))
                .fetch();
    }

    @Override
    public Item findItemOptionFetchById(Long itemId){
        return queryFactory
                .selectFrom(item)
                .distinct()
                .leftJoin(item.itemOptionList, itemOption).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();
    }
    @Override
    public Item findItemOptionStoreAuthTrueFetchById(Long itemId) {
        return queryFactory
                .selectFrom(item)
                .distinct()
                .leftJoin(item.itemOptionList, itemOption).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(item.id.eq(itemId).and(storeOwner.managerAuthenticated.isTrue()))
                .fetchOne();
    }
    @Override
    public Item findItemOptionCategoryFetchById(Long itemId){
        return queryFactory
                .selectFrom(item)
                .distinct()
                .join(item.category, category).fetchJoin()
                .leftJoin(item.itemOptionList, itemOption).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();
    }

    @Override
    public Item findStoreFetchById(Long itemId) {
        return queryFactory
                .selectFrom(item)
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(item.id.eq(itemId))
                .fetchOne();
    }
}
