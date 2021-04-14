package com.devgyu.banchan.review.query;

import com.devgyu.banchan.review.QReview;
import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.dto.ReviewFetchDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
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
    public Page<ReviewFetchDto> findStoreReviewAccountOrdersOrderItemItemStoreLeftByAccountId(Long accountId, Pageable pageable){
        QReview storeReview = new QReview("storeReview");
        QueryResults<ReviewFetchDto> result = queryFactory
                .select(Projections.constructor(ReviewFetchDto.class, review.id, storeOwner.id, review.regDate, review.content, review.starPoint,
                        account.thumbnail, account.nickname, account.role, storeOwner.nickname, storeOwner.thumbnail, review.storeReview.content))
                .from(review)
                .leftJoin(review.storeReview, storeReview)
                .join(review.account, account)
                .join(review.orders, orders)
                .join(orders.ordersItemList, ordersItem)
                .join(ordersItem.item, item)
                .join(item.storeOwner, storeOwner)
                .where(account.id.eq(accountId))
                .orderBy(review.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ReviewFetchDto> findAccountOrdersOrderItemItemStoreByStoreId(Long storeId, Pageable pageable) {
        QReview newReview = new QReview("temp");
        QueryResults<ReviewFetchDto> results = queryFactory
                .select(Projections.constructor(ReviewFetchDto.class, review.id, storeOwner.id, review.regDate, review.content,
                        review.starPoint, account.thumbnail, account.nickname, account.role, storeOwner.nickname, storeOwner.thumbnail, review.storeReview.content))
                .from(review)
                .distinct()
                .leftJoin(review.storeReview, newReview)
                .join(review.account, account)
                .join(review.orders, orders)
                .join(orders.ordersItemList, ordersItem)
                .join(ordersItem.item, item)
                .join(item.storeOwner, storeOwner)
                .where(storeOwner.id.eq(storeId))
                .orderBy(review.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
