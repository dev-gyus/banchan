package com.devgyu.banchan.orders.query;

import com.devgyu.banchan.modules.rider.QRider;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.modules.rider.QRiderOrders.riderOrders;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;
import static com.devgyu.banchan.orders.QOrders.orders;
import static com.devgyu.banchan.ordersitem.QOrdersItem.ordersItem;

public class OrdersRepositoryImpl implements OrdersQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public OrdersRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Orders findRiderLeftFetchByOrderId(Long ordersId) {
        return queryFactory
                .selectFrom(orders)
                .leftJoin(orders.riderOrders, riderOrders).fetchJoin()
                .leftJoin(riderOrders.rider, QRider.rider).fetchJoin()
                .where(orders.id.eq(ordersId))
                .fetchOne();
    }

    @Override
    public Page<Orders> findAccountItemStoreFetchByIdAndStatus(Long accountId, @Nullable Pageable pageable, OrderStatus firstCondition,
                                                          @Nullable OrderStatus secondCondition, @Nullable OrderStatus thirdCondition,
                                                          @Nullable OrderStatus fourthCondition){
        QueryResults<Orders> result = queryFactory
                .selectFrom(orders)
                .distinct()
                .join(orders.account, account).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(account.id.eq(accountId).and(conditionSelect(firstCondition, secondCondition, thirdCondition, fourthCondition)))
                .orderBy(orders.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    @Override
    public Page<Orders> findAccountItemFetchByStoreIdAndStatus(Long storeOwnerId, Pageable pageable, OrderStatus firstCondition,
                                                          @Nullable OrderStatus secondCondition, @Nullable OrderStatus thirdCondition){
        QueryResults<Orders> result = queryFactory
                .selectFrom(orders)
                .distinct()
                .join(orders.account, account).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(storeOwner.id.eq(storeOwnerId).and(conditionSelect(firstCondition, secondCondition, thirdCondition, null)))
                .orderBy(orders.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    private BooleanExpression conditionSelect(OrderStatus firstCondition,
                                              @Nullable OrderStatus secondCondition, @Nullable OrderStatus thirdCondition,
                                              @Nullable OrderStatus fourthCondition){
        return secondCondition == null ? orders.orderStatus.eq(firstCondition) :
                thirdCondition == null ? orders.orderStatus.eq(firstCondition).or(orders.orderStatus.eq(secondCondition)) :
                fourthCondition == null ? orders.orderStatus.eq(firstCondition).or(orders.orderStatus.eq(secondCondition))
                .or(orders.orderStatus.eq(thirdCondition)) : orders.orderStatus.eq(firstCondition).or(orders.orderStatus.eq(secondCondition))
                        .or(orders.orderStatus.eq(thirdCondition)).or(orders.orderStatus.eq(fourthCondition));
    }

    @Override
    public Page<Orders> findAccountItemFetchByStoreRoadIdAndStatus(String doSigungu, OrderStatus orderStatus, Pageable pageable){
        QueryResults<Orders> results = queryFactory
                .selectFrom(orders)
                .join(orders.account, account).fetchJoin()
                .join(orders.ordersItemList, ordersItem).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(storeOwner.address.road.contains(doSigungu).and(orders.orderStatus.eq(orderStatus)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(results.getResults(), pageable, results.getTotal());

    }

    @Override
    public List<Orders> findAccountFetchById(Long ordersId){
        return queryFactory
                .selectFrom(orders)
                .join(orders.account, account).fetchJoin()
                .where(orders.id.eq(ordersId))
                .fetch();
    }


}
