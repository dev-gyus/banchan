package com.devgyu.banchan.ordersitem.query;

import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.items.QItemOption.itemOption;
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
}
