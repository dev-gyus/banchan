package com.devgyu.banchan.modules.category.query;

import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.QCategory;
import com.devgyu.banchan.modules.storecategory.QStoreCategory;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.modules.category.QCategory.category;
import static com.devgyu.banchan.modules.storecategory.QStoreCategory.storeCategory;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;

public class CategoryRepositoryImpl implements CategoryQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CategoryRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public List<Category> findAllByStoreOwnerId(Long storeOwnerId){
        return queryFactory
                .selectFrom(category)
                .join(category.storeCategoryList, storeCategory).fetchJoin()
                .join(storeCategory.storeOwner, storeOwner).fetchJoin()
                .where(storeOwner.id.eq(storeOwnerId))
                .fetch();
    }
}
