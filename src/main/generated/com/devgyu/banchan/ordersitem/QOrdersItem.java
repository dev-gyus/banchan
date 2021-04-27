package com.devgyu.banchan.ordersitem;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrdersItem is a Querydsl query type for OrdersItem
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrdersItem extends EntityPathBase<OrdersItem> {

    private static final long serialVersionUID = 116379022L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrdersItem ordersItem = new QOrdersItem("ordersItem");

    public final DateTimePath<java.time.LocalDateTime> addDate = createDateTime("addDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.devgyu.banchan.items.QItem item;

    public final ListPath<com.devgyu.banchan.items.ItemOption, com.devgyu.banchan.items.QItemOption> itemOptionList = this.<com.devgyu.banchan.items.ItemOption, com.devgyu.banchan.items.QItemOption>createList("itemOptionList", com.devgyu.banchan.items.ItemOption.class, com.devgyu.banchan.items.QItemOption.class, PathInits.DIRECT2);

    public final com.devgyu.banchan.orders.QOrders orders;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QOrdersItem(String variable) {
        this(OrdersItem.class, forVariable(variable), INITS);
    }

    public QOrdersItem(Path<? extends OrdersItem> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrdersItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrdersItem(PathMetadata metadata, PathInits inits) {
        this(OrdersItem.class, metadata, inits);
    }

    public QOrdersItem(Class<? extends OrdersItem> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new com.devgyu.banchan.items.QItem(forProperty("item"), inits.get("item")) : null;
        this.orders = inits.isInitialized("orders") ? new com.devgyu.banchan.orders.QOrders(forProperty("orders"), inits.get("orders")) : null;
    }

}

