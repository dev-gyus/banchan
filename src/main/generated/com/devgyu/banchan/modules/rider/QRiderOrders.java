package com.devgyu.banchan.modules.rider;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRiderOrders is a Querydsl query type for RiderOrders
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QRiderOrders extends EntityPathBase<RiderOrders> {

    private static final long serialVersionUID = -792498332L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRiderOrders riderOrders = new QRiderOrders("riderOrders");

    public final NumberPath<Integer> deliveryPrice = createNumber("deliveryPrice", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.devgyu.banchan.orders.QOrders orders;

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final QRider rider;

    public QRiderOrders(String variable) {
        this(RiderOrders.class, forVariable(variable), INITS);
    }

    public QRiderOrders(Path<? extends RiderOrders> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRiderOrders(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRiderOrders(PathMetadata metadata, PathInits inits) {
        this(RiderOrders.class, metadata, inits);
    }

    public QRiderOrders(Class<? extends RiderOrders> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.orders = inits.isInitialized("orders") ? new com.devgyu.banchan.orders.QOrders(forProperty("orders"), inits.get("orders")) : null;
        this.rider = inits.isInitialized("rider") ? new QRider(forProperty("rider"), inits.get("rider")) : null;
    }

}

