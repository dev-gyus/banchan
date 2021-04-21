package com.devgyu.banchan.account.customer;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.devgyu.banchan.account.chatroom.ChatRoom;
import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCustomer is a Querydsl query type for Customer
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCustomer extends EntityPathBase<Customer> {

    private static final long serialVersionUID = -1606872019L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCustomer customer = new QCustomer("customer");

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

    public QCustomer(String variable) {
        this(Customer.class, forVariable(variable), INITS);
    }

    public QCustomer(Path<? extends Customer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCustomer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCustomer(PathMetadata metadata, PathInits inits) {
        this(Customer.class, metadata, inits);
    }

    public QCustomer(Class<? extends Customer> type, PathMetadata metadata, PathInits inits) {
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

