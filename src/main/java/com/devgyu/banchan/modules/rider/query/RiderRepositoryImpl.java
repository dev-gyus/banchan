package com.devgyu.banchan.modules.rider.query;

import com.devgyu.banchan.modules.rider.QRider;
import com.devgyu.banchan.modules.rider.QRiderOrders;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderOrderDto;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.modules.rider.QRider.rider;
import static com.devgyu.banchan.modules.rider.QRiderOrders.riderOrders;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;
import static com.devgyu.banchan.orders.QOrders.orders;
import static com.devgyu.banchan.ordersitem.QOrdersItem.ordersItem;

public class RiderRepositoryImpl implements RiderQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public RiderRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RiderOrderDto> findAccountItemFetchByStoreRoadIdAndStatus(String doSigungu, OrderStatus orderStatus, Pageable pageable){
        QueryResults<RiderOrderDto> results = queryFactory
                .select(Projections.constructor(RiderOrderDto.class, orders.id, account.address.road,
                        account.address.detail, storeOwner.nickname, storeOwner.address.road,
                        storeOwner.address.detail, orders.regDate))
                .from(orders)
                .join(orders.account, account)
                .join(orders.ordersItemList, ordersItem)
                .join(ordersItem.item, item)
                .join(item.storeOwner, storeOwner)
                .where(storeOwner.address.road.contains(doSigungu).and(orders.orderStatus.eq(orderStatus)))
                .orderBy(orders.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());

    }

    @Override
    public Page<RiderOrderDto> findAccountItemFetchByRiderIdAndStatus(Long id, OrderStatus orderStatus, Pageable pageable){
        QueryResults<RiderOrderDto> results = queryFactory
                .select(Projections.constructor(RiderOrderDto.class, orders.id, account.address.road,
                        account.address.detail, storeOwner.nickname, storeOwner.address.road,
                        storeOwner.address.detail, orders.regDate))
                .from(orders)
                .join(orders.account, account)
                .join(orders.riderOrders, riderOrders)
                .join(riderOrders.rider, rider)
                .join(orders.ordersItemList, ordersItem)
                .join(ordersItem.item, item)
                .join(item.storeOwner, storeOwner)
                .where(rider.id.eq(id).and(orders.orderStatus.eq(orderStatus)))
                .orderBy(orders.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());

    }

    @Override
    public Page<Rider> findAllByManagerAuthenticatedAndSigungu(boolean managerAuth, String sigungu, Pageable pageable) {
        QueryResults<Rider> result = queryFactory
                .selectFrom(rider)
                .where(rider.managerAuthenticated.eq(managerAuth).and(rider.address.road.contains(sigungu)))
                .orderBy(rider.lastAuthDate.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}
