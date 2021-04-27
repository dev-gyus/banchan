package com.devgyu.banchan.items;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = -1594809599L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final com.devgyu.banchan.modules.category.QCategory category;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath itemIntroduce = createString("itemIntroduce");

    public final ListPath<ItemOption, QItemOption> itemOptionList = this.<ItemOption, QItemOption>createList("itemOptionList", ItemOption.class, QItemOption.class, PathInits.DIRECT2);

    public final EnumPath<ItemStatus> itemStatus = createEnum("itemStatus", ItemStatus.class);

    public final StringPath name = createString("name");

    public final ListPath<com.devgyu.banchan.ordersitem.OrdersItem, com.devgyu.banchan.ordersitem.QOrdersItem> ordersItemList = this.<com.devgyu.banchan.ordersitem.OrdersItem, com.devgyu.banchan.ordersitem.QOrdersItem>createList("ordersItemList", com.devgyu.banchan.ordersitem.OrdersItem.class, com.devgyu.banchan.ordersitem.QOrdersItem.class, PathInits.DIRECT2);

    public final StringPath originThumbnail = createString("originThumbnail");

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final BooleanPath soldOut = createBoolean("soldOut");

    public final com.devgyu.banchan.modules.storeowner.QStoreOwner storeOwner;

    public final StringPath thumbnail = createString("thumbnail");

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.devgyu.banchan.modules.category.QCategory(forProperty("category")) : null;
        this.storeOwner = inits.isInitialized("storeOwner") ? new com.devgyu.banchan.modules.storeowner.QStoreOwner(forProperty("storeOwner"), inits.get("storeOwner")) : null;
    }

}

