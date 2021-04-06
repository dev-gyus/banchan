package com.devgyu.banchan.modules.storecategory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreCategory is a Querydsl query type for StoreCategory
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QStoreCategory extends EntityPathBase<StoreCategory> {

    private static final long serialVersionUID = 719569353L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreCategory storeCategory = new QStoreCategory("storeCategory");

    public final com.devgyu.banchan.modules.category.QCategory category;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.devgyu.banchan.modules.storeowner.QStoreOwner storeOwner;

    public QStoreCategory(String variable) {
        this(StoreCategory.class, forVariable(variable), INITS);
    }

    public QStoreCategory(Path<? extends StoreCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreCategory(PathMetadata metadata, PathInits inits) {
        this(StoreCategory.class, metadata, inits);
    }

    public QStoreCategory(Class<? extends StoreCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.devgyu.banchan.modules.category.QCategory(forProperty("category")) : null;
        this.storeOwner = inits.isInitialized("storeOwner") ? new com.devgyu.banchan.modules.storeowner.QStoreOwner(forProperty("storeOwner"), inits.get("storeOwner")) : null;
    }

}

