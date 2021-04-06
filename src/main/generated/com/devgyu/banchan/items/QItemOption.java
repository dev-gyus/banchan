package com.devgyu.banchan.items;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemOption is a Querydsl query type for ItemOption
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItemOption extends EntityPathBase<ItemOption> {

    private static final long serialVersionUID = -1537174826L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemOption itemOption = new QItemOption("itemOption");

    public final ListPath<com.devgyu.banchan.cart.CartItem, com.devgyu.banchan.cart.QCartItem> cartItemList = this.<com.devgyu.banchan.cart.CartItem, com.devgyu.banchan.cart.QCartItem>createList("cartItemList", com.devgyu.banchan.cart.CartItem.class, com.devgyu.banchan.cart.QCartItem.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final StringPath name = createString("name");

    public final ListPath<com.devgyu.banchan.ordersitem.OrdersItem, com.devgyu.banchan.ordersitem.QOrdersItem> ordersItemList = this.<com.devgyu.banchan.ordersitem.OrdersItem, com.devgyu.banchan.ordersitem.QOrdersItem>createList("ordersItemList", com.devgyu.banchan.ordersitem.OrdersItem.class, com.devgyu.banchan.ordersitem.QOrdersItem.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public QItemOption(String variable) {
        this(ItemOption.class, forVariable(variable), INITS);
    }

    public QItemOption(Path<? extends ItemOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemOption(PathMetadata metadata, PathInits inits) {
        this(ItemOption.class, metadata, inits);
    }

    public QItemOption(Class<? extends ItemOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
    }

}

