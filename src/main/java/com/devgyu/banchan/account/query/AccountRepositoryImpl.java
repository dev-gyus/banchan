package com.devgyu.banchan.account.query;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.QAccount;
import com.devgyu.banchan.cart.QCart;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.cart.QCart.cart;

public class AccountRepositoryImpl implements AccountQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AccountRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Account findCartFetchById(Long id){
        return queryFactory
                .selectFrom(account)
                .join(account.cart, cart).fetchJoin()
                .where(account.id.eq(id))
                .fetchOne();
    }
}
