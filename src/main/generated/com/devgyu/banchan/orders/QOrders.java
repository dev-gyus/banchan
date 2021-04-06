package com.devgyu.banchan.orders;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrders is a Querydsl query type for Orders
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrders extends EntityPathBase<Orders> {

    private static final long serialVersionUID = -426979378L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrders orders = new QOrders("orders");

    public final com.devgyu.banchan.account.QAccount account;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.devgyu.banchan.ordersitem.OrdersItem, com.devgyu.banchan.ordersitem.QOrdersItem> ordersItemList = this.<com.devgyu.banchan.ordersitem.OrdersItem, com.devgyu.banchan.ordersitem.QOrdersItem>createList("ordersItemList", com.devgyu.banchan.ordersitem.OrdersItem.class, com.devgyu.banchan.ordersitem.QOrdersItem.class, PathInits.DIRECT2);

    public final EnumPath<OrderStatus> orderStatus = createEnum("orderStatus", OrderStatus.class);

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final BooleanPath reviewed = createBoolean("reviewed");

    public final com.devgyu.banchan.modules.rider.QRiderOrders riderOrders;

    public final NumberPath<Integer> totalPrice = createNumber("totalPrice", Integer.class);

    public QOrders(String variable) {
        this(Orders.class, forVariable(variable), INITS);
    }

    public QOrders(Path<? extends Orders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrders(PathMetadata metadata, PathInits inits) {
        this(Orders.class, metadata, inits);
    }

    public QOrders(Class<? extends Orders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.devgyu.banchan.account.QAccount(forProperty("account"), inits.get("account")) : null;
        this.riderOrders = inits.isInitialized("riderOrders") ? new com.devgyu.banchan.modules.rider.QRiderOrders(forProperty("riderOrders"), inits.get("riderOrders")) : null;
    }

}

