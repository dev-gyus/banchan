package com.devgyu.banchan.items.query;

import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.modules.category.QCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.modules.category.QCategory.category;

public class ItemRepositoryImpl implements ItemQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Item> findAllByCategoryName(String categoryName){
        return queryFactory
                .selectFrom(item)
                .join(item.category, category).fetchJoin()
                .where(category.name.eq(categoryName))
                .fetch();
    }
}
