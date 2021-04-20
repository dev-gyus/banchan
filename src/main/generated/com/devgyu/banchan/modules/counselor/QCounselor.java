package com.devgyu.banchan.modules.counselor;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCounselor is a Querydsl query type for Counselor
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCounselor extends EntityPathBase<Counselor> {

    private static final long serialVersionUID = -364267557L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCounselor counselor = new QCounselor("counselor");

    public final com.devgyu.banchan.account.QAccount _super;

    // inherited
    public final com.devgyu.banchan.account.QAddress address;

    //inherited
    public final ListPath<com.devgyu.banchan.alarm.Alarm, com.devgyu.banchan.alarm.QAlarm> alarmList;

    //inherited
    public final NumberPath<Integer> blockCount;

    //inherited
    public final BooleanPath blocked;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> blockedDate;

    //inherited
    public final StringPath blockReason;

    // inherited
    public final com.devgyu.banchan.cart.QCart cart;

    //inherited
    public final StringPath email;

    //inherited
    public final StringPath emailToken;

    //inherited
    public final NumberPath<Integer> failCount;

    //inherited
    public final NumberPath<Long> id;

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

    //inherited
    public final StringPath thumbnail;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> unblockedDate;

    public QCounselor(String variable) {
        this(Counselor.class, forVariable(variable), INITS);
    }

    public QCounselor(Path<? extends Counselor> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCounselor(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCounselor(PathMetadata metadata, PathInits inits) {
        this(Counselor.class, metadata, inits);
    }

    public QCounselor(Class<? extends Counselor> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.devgyu.banchan.account.QAccount(type, metadata, inits);
        this.address = _super.address;
        this.alarmList = _super.alarmList;
        this.blockCount = _super.blockCount;
        this.blocked = _super.blocked;
        this.blockedDate = _super.blockedDate;
        this.blockReason = _super.blockReason;
        this.cart = _super.cart;
        this.email = _super.email;
        this.emailToken = _super.emailToken;
        this.failCount = _super.failCount;
        this.id = _super.id;
        this.name = _super.name;
        this.nickname = _super.nickname;
        this.ordersList = _super.ordersList;
        this.password = _super.password;
        this.phone = _super.phone;
        this.reviewList = _super.reviewList;
        this.role = _super.role;
        this.thumbnail = _super.thumbnail;
        this.unblockedDate = _super.unblockedDate;
    }

}

