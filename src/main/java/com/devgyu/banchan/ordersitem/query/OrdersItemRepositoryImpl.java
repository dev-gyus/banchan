package com.devgyu.banchan.ordersitem.query;

import com.devgyu.banchan.ordersitem.OrdersItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.items.QItemOption.itemOption;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;
import static com.devgyu.banchan.orders.QOrders.orders;
import static com.devgyu.banchan.ordersitem.QOrdersItem.ordersItem;

public class OrdersItemRepositoryImpl implements OrdersItemQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public OrdersItemRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<OrdersItem> findItemOptionFetchByIdIn(List<Long> orderItemIdList){
        return queryFactory
                .selectFrom(ordersItem)
                .leftJoin(ordersItem.itemOptionList, itemOption).fetchJoin()
                .where(ordersItem.id.in(orderItemIdList))
                .fetch();
    }

    public List<OrdersItem> findAccountOrderItemOptionStoreOwnerByOrderIdAndRiderRoad(Long orderId){
        return queryFactory
                .selectFrom(ordersItem)
                .leftJoin(ordersItem.itemOptionList, itemOption).fetchJoin()
                .join(ordersItem.item, item).fetchJoin()
                .join(ordersItem.orders, orders).fetchJoin()
                .join(orders.account, account).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .where(orders.id.eq(orderId))
                .fetch();
    }
}
