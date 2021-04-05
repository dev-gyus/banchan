package com.devgyu.banchan.cart.query;

import com.devgyu.banchan.account.QAccount;
import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.cart.QCart;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.cart.QCart.cart;

public class CartRepositoryImpl implements CartQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public CartRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Cart> existByAccountId(Long accountId){
        return queryFactory
                .selectFrom(cart)
                .join(cart.account, account).fetchJoin()
                .where(account.id.eq(accountId))
                .fetch();
    }
}
