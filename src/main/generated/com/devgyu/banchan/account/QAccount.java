package com.devgyu.banchan.account;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAccount is a Querydsl query type for Account
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QAccount extends EntityPathBase<Account> {

    private static final long serialVersionUID = -1391349524L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAccount account = new QAccount("account");

    public final QAddress address;

    public final ListPath<com.devgyu.banchan.alarm.Alarm, com.devgyu.banchan.alarm.QAlarm> alarmList = this.<com.devgyu.banchan.alarm.Alarm, com.devgyu.banchan.alarm.QAlarm>createList("alarmList", com.devgyu.banchan.alarm.Alarm.class, com.devgyu.banchan.alarm.QAlarm.class, PathInits.DIRECT2);

    public final NumberPath<Integer> blockCount = createNumber("blockCount", Integer.class);

    public final BooleanPath blocked = createBoolean("blocked");

    public final DateTimePath<java.time.LocalDateTime> blockedDate = createDateTime("blockedDate", java.time.LocalDateTime.class);

    public final StringPath blockReason = createString("blockReason");

    public final com.devgyu.banchan.cart.QCart cart;

    public final StringPath email = createString("email");

    public final StringPath emailToken = createString("emailToken");

    public final NumberPath<Integer> failCount = createNumber("failCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final ListPath<com.devgyu.banchan.orders.Orders, com.devgyu.banchan.orders.QOrders> ordersList = this.<com.devgyu.banchan.orders.Orders, com.devgyu.banchan.orders.QOrders>createList("ordersList", com.devgyu.banchan.orders.Orders.class, com.devgyu.banchan.orders.QOrders.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final ListPath<com.devgyu.banchan.review.Review, com.devgyu.banchan.review.QReview> reviewList = this.<com.devgyu.banchan.review.Review, com.devgyu.banchan.review.QReview>createList("reviewList", com.devgyu.banchan.review.Review.class, com.devgyu.banchan.review.QReview.class, PathInits.DIRECT2);

    public final EnumPath<Roles> role = createEnum("role", Roles.class);

    public final StringPath thumbnail = createString("thumbnail");

    public final DateTimePath<java.time.LocalDateTime> unblockedDate = createDateTime("unblockedDate", java.time.LocalDateTime.class);

    public QAccount(String variable) {
        this(Account.class, forVariable(variable), INITS);
    }

    public QAccount(Path<? extends Account> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAccount(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAccount(PathMetadata metadata, PathInits inits) {
        this(Account.class, metadata, inits);
    }

    public QAccount(Class<? extends Account> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.address = inits.isInitialized("address") ? new QAddress(forProperty("address")) : null;
        this.cart = inits.isInitialized("cart") ? new com.devgyu.banchan.cart.QCart(forProperty("cart"), inits.get("cart")) : null;
    }

}

