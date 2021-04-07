package com.devgyu.banchan.review.query;

import com.devgyu.banchan.account.QAccount;
import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.devgyu.banchan.orders.QOrders;
import com.devgyu.banchan.ordersitem.QOrdersItem;
import com.devgyu.banchan.review.QReview;
import com.devgyu.banchan.review.Review;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;
import static com.devgyu.banchan.orders.QOrders.orders;
import static com.devgyu.banchan.ordersitem.QOrdersItem.ordersItem;
import static com.devgyu.banchan.review.QReview.review;

public class ReviewRepositoryImpl implements ReviewQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ReviewRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Review> findAccountOrdersOrderItemItemStoreByAccountId(Long accountId, Pageable pageable){
        QueryResults<Review> results = queryFactory
                .selectFrom(review)
                .join(review.account, account).fetchJoin()
                .join(review.orders, orders).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(account.id.eq(accountId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public Page<Review> findAccountOrdersOrderItemItemStoreByStoreId(Long storeId, Pageable pageable) {
        QReview newReview = new QReview("temp");
        QueryResults<Review> results = queryFactory
                .selectFrom(review)
                .distinct()
                .leftJoin(review.storeReview, newReview).fetchJoin()
                .join(review.account).fetchJoin()
                .join(review.orders, orders).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(storeOwner.id.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
