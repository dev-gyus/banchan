package com.devgyu.banchan.modules.storecategory.query;

import com.devgyu.banchan.modules.category.QCategory;
import com.devgyu.banchan.modules.storecategory.QStoreCategory;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.modules.category.QCategory.category;
import static com.devgyu.banchan.modules.storecategory.QStoreCategory.storeCategory;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;

public class StoreCategoryRepositoryImpl implements StoreCategoryQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public StoreCategoryRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }
}
