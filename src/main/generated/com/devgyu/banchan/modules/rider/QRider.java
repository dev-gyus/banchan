package com.devgyu.banchan.modules.rider;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRider is a Querydsl query type for Rider
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRider extends EntityPathBase<Rider> {

    private static final long serialVersionUID = -1745185889L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRider rider = new QRider("rider");

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
    public final ListPath<ChatRoom, com.devgyu.banchan.chat.QChatRoom> chatRoomList;

    public final StringPath driverLicense = createString("driverLicense");

    //inherited
    public final StringPath email;

    //inherited
    public final StringPath emailToken;

    //inherited
    public final NumberPath<Integer> failCount;

    //inherited
    public final NumberPath<Long> id;

    public final DateTimePath<java.time.LocalDateTime> lastAuthDate = createDateTime("lastAuthDate", java.time.LocalDateTime.class);

    public final BooleanPath managerAuthenticated = createBoolean("managerAuthenticated");

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

    public final ListPath<RiderOrders, QRiderOrders> riderOrdersList = this.<RiderOrders, QRiderOrders>createList("riderOrdersList", RiderOrders.class, QRiderOrders.class, PathInits.DIRECT2);

    //inherited
    public final EnumPath<com.devgyu.banchan.account.Roles> role;

    //inherited
    public final StringPath thumbnail;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> unblockedDate;

    public QRider(String variable) {
        this(Rider.class, forVariable(variable), INITS);
    }

    public QRider(Path<? extends Rider> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRider(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRider(PathMetadata metadata, PathInits inits) {
        this(Rider.class, metadata, inits);
    }

    public QRider(Class<? extends Rider> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this._super = new com.devgyu.banchan.account.QAccount(type, metadata, inits);
        this.address = _super.address;
        this.alarmList = _super.alarmList;
        this.blockCount = _super.blockCount;
        this.blocked = _super.blocked;
        this.blockedDate = _super.blockedDate;
        this.blockReason = _super.blockReason;
        this.cart = _super.cart;
        this.chatRoomList = _super.chatRoomList;
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

