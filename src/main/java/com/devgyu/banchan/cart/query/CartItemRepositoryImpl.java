package com.devgyu.banchan.cart.query;

import com.devgyu.banchan.account.QAccount;
import com.devgyu.banchan.cart.CartItem;
import com.devgyu.banchan.cart.QCart;
import com.devgyu.banchan.cart.QCartItem;
import com.devgyu.banchan.items.QItem;
import com.devgyu.banchan.items.QItemOption;
import com.devgyu.banchan.modules.storeowner.QStoreOwner;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.cart.QCart.cart;
import static com.devgyu.banchan.cart.QCartItem.cartItem;
import static com.devgyu.banchan.items.QItem.item;
import static com.devgyu.banchan.items.QItemOption.itemOption;
import static com.devgyu.banchan.modules.storeowner.QStoreOwner.storeOwner;

public class CartItemRepositoryImpl implements CartItemQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CartItemRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<CartItem> findAccountCartItemItemOptionFetchByAccountId(Long id, Pageable pageable){
        QueryResults<CartItem> result = queryFactory
                .selectFrom(cartItem)
                .distinct()
                .join(cartItem.cart, cart).fetchJoin()
                .join(cartItem.item, item).fetchJoin()
                .join(item.storeOwner, storeOwner).fetchJoin()
                .leftJoin(cartItem.itemOptionList, itemOption).fetchJoin()
                .join(cart.account, account).fetchJoin()
                .where(account.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
    @Override
    public List<CartItem> findAccountCartFetchByAccountId(Long accountId){
        return queryFactory
                .selectFrom(cartItem)
                .distinct()
                .join(cartItem.cart, cart).fetchJoin()
                .join(cart.account, account).fetchJoin()
                .join(cartItem.item, item).fetchJoin()
                .leftJoin(cartItem.itemOptionList, itemOption).fetchJoin()
                .where(account.id.eq(accountId))
                .fetch();
    }
}
