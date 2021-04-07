package com.devgyu.banchan.modules.storeowner;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreOwner is a Querydsl query type for StoreOwner
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStoreOwner extends EntityPathBase<StoreOwner> {

    private static final long serialVersionUID = 211044839L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreOwner storeOwner = new QStoreOwner("storeOwner");

    public final com.devgyu.banchan.account.QAccount _super;

    // inherited
    public final com.devgyu.banchan.account.QAddress address;

    // inherited
    public final com.devgyu.banchan.cart.QCart cart;

    //inherited
    public final StringPath email;

    //inherited
    public final StringPath emailToken;

    //inherited
    public final NumberPath<Long> id;

    public final ListPath<com.devgyu.banchan.items.Item, com.devgyu.banchan.items.QItem> items = this.<com.devgyu.banchan.items.Item, com.devgyu.banchan.items.QItem>createList("items", com.devgyu.banchan.items.Item.class, com.devgyu.banchan.items.QItem.class, PathInits.DIRECT2);

    //inherited
    public final StringPath name;

    //inherited
    public final StringPath nickname;

    //inherited
    public final ListPath<com.devgyu.banchan.orders.Orders, com.devgyu.banchan.orders.QOrders> ordersList;

    //inherited
    public final StringPath password;

    //inherited
    public final StringPath phone;

    //inherited
    public final ListPath<com.devgyu.banchan.review.Review, com.devgyu.banchan.review.QReview> reviewList;

    //inherited
    public final EnumPath<com.devgyu.banchan.account.Roles> role;

    public final ListPath<com.devgyu.banchan.modules.storecategory.StoreCategory, com.devgyu.banchan.modules.storecategory.QStoreCategory> storeCategories = this.<com.devgyu.banchan.modules.storecategory.StoreCategory, com.devgyu.banchan.modules.storecategory.QStoreCategory>createList("storeCategories", com.devgyu.banchan.modules.storecategory.StoreCategory.class, com.devgyu.banchan.modules.storecategory.QStoreCategory.class, PathInits.DIRECT2);

    public final StringPath storeIntroduce = createString("storeIntroduce");

    public final StringPath tel = createString("tel");

    //inherited
    public final StringPath thumbnail;

    public QStoreOwner(String variable) {
        this(StoreOwner.class, forVariable(variable), INITS);
    }

    public QStoreOwner(Path<? extends StoreOwner> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreOwner(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreOwner(PathMetadata metadata, PathInits inits) {
        this(StoreOwner.class, metadata, inits);
    }

    public QStoreOwner(Class<? extends StoreOwner> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.devgyu.banchan.account.QAccount(type, metadata, inits);
        this.address = _super.address;
        this.cart = _super.cart;
        this.email = _super.email;
        this.emailToken = _super.emailToken;
        this.id = _super.id;
        this.name = _super.name;
        this.nickname = _super.nickname;
        this.ordersList = _super.ordersList;
        this.password = _super.password;
        this.phone = _super.phone;
        this.reviewList = _super.reviewList;
        this.role = _super.role;
        this.thumbnail = _super.thumbnail;
    }

}

