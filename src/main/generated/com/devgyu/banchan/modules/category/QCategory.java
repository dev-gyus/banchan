package com.devgyu.banchan.modules.category;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -429223161L;

    public static final QCategory category = new QCategory("category");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.devgyu.banchan.items.Item, com.devgyu.banchan.items.QItem> items = this.<com.devgyu.banchan.items.Item, com.devgyu.banchan.items.QItem>createList("items", com.devgyu.banchan.items.Item.class, com.devgyu.banchan.items.QItem.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final ListPath<com.devgyu.banchan.modules.storecategory.StoreCategory, com.devgyu.banchan.modules.storecategory.QStoreCategory> storeCategoryList = this.<com.devgyu.banchan.modules.storecategory.StoreCategory, com.devgyu.banchan.modules.storecategory.QStoreCategory>createList("storeCategoryList", com.devgyu.banchan.modules.storecategory.StoreCategory.class, com.devgyu.banchan.modules.storecategory.QStoreCategory.class, PathInits.DIRECT2);

    public final StringPath urlName = createString("urlName");

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

