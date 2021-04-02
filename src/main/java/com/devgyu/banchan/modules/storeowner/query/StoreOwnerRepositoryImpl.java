package com.devgyu.banchan.modules.storeowner.query;

import com.devgyu.banchan.modules.category.QCategory;
import com.devgyu.banchan.modules.storecategory.QStoreCategory;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.modules.category.QCategory.category;
import static com.devgyu.banchan.modules.storecategory.QStoreCategory.storeCategory;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;

public class StoreOwnerRepositoryImpl implements StoreOwnerQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public StoreOwnerRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<StoreOwner> findCategoriesFetchById(Long id){
        return queryFactory
                .selectFrom(storeOwner)
                .leftJoin(storeOwner.storeCategories, storeCategory).fetchJoin()
                .leftJoin(storeCategory.category, category).fetchJoin()
                .where(storeOwner.id.eq(id))
                .fetch();
    }

    public Page<StoreOwner> findCategoriesFetchByCategoryName(String categoryName, Pageable pageable){
        QueryResults<StoreOwner> findStoreOwner = queryFactory
                .selectFrom(storeOwner)
                .join(storeOwner.storeCategories, storeCategory).fetchJoin()
                .join(storeCategory.category, category).fetchJoin()
                .where(category.urlName.eq(categoryName))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(findStoreOwner.getResults(), pageable, findStoreOwner.getTotal());
    }
}