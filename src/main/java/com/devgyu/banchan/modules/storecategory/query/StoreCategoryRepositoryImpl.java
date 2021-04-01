package com.devgyu.banchan.modules.storecategory.query;

import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

public class StoreCategoryRepositoryImpl implements StoreCategoryQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

}
