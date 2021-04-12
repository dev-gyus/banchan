package com.devgyu.banchan.modules.rider.query;

import com.devgyu.banchan.modules.rider.RiderOrders;
import com.devgyu.banchan.orders.OrderStatus;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.modules.rider.QRider.rider;
import static com.devgyu.banchan.modules.rider.QRiderOrders.riderOrders;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;
import static com.devgyu.banchan.orders.QOrders.orders;
import static com.devgyu.banchan.ordersitem.QOrdersItem.ordersItem;

public class RiderOrdersRepositoryImpl implements RiderOrdersQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public RiderOrdersRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RiderOrders> findAccountOrdersItemStoreFetchByRiderIdAndStatus(Long riderId, OrderStatus orderStatus, Pageable pageable){
        QueryResults<RiderOrders> results = queryFactory
                .selectFrom(riderOrders)
                .join(riderOrders.orders, orders).fetchJoin()
                .join(riderOrders.rider, rider).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(orders.account, account).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(rider.id.eq(riderId).and(orders.orderStatus.eq(orderStatus)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    @Override
    public List<RiderOrders> findAccountOrdersItemStoreFetchByOrdersIdAndRiderId(Long ordersId, Long riderId){
        return queryFactory
                .selectFrom(riderOrders)
                .join(riderOrders.rider, rider)
                .join(riderOrders.orders, orders).fetchJoin()
                .join(orders.account, account).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(orders.id.eq(ordersId).and(rider.id.eq(riderId)))
                .fetch();
    }

    @Override
    public List<RiderOrders> findOrderFetchByRiderIdAndOrderId(Long riderId, Long orderId) {
        return queryFactory
                .selectFrom(riderOrders)
                .join(riderOrders.rider, rider).fetchJoin()
                .join(riderOrders.orders, orders).fetchJoin()
                .where(rider.id.eq(riderId).and(orders.id.eq(orderId)))
                .fetch();
    }
}
