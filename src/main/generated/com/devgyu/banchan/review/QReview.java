package com.devgyu.banchan.review;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = -1278904210L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final com.devgyu.banchan.account.QAccount account;

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.devgyu.banchan.orders.QOrders orders;

    public final DateTimePath<java.time.LocalDateTime> regDate = createDateTime("regDate", java.time.LocalDateTime.class);

    public final EnumPath<ReviewStatus> reviewStatus = createEnum("reviewStatus", ReviewStatus.class);

    public final NumberPath<Integer> starPoint = createNumber("starPoint", Integer.class);

    public final QReview storeReview;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.account = inits.isInitialized("account") ? new com.devgyu.banchan.account.QAccount(forProperty("account"), inits.get("account")) : null;
        this.orders = inits.isInitialized("orders") ? new com.devgyu.banchan.orders.QOrders(forProperty("orders"), inits.get("orders")) : null;
        this.storeReview = inits.isInitialized("storeReview") ? new QReview(forProperty("storeReview"), inits.get("storeReview")) : null;
    }

}

