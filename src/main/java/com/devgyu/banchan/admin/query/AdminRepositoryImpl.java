package com.devgyu.banchan.admin.query;


import com.devgyu.banchan.account.Account;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;

public class AdminRepositoryImpl implements AdminQueryRepository{
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public AdminRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Account> findAccountByConditions(String firstCondition, String secondCondition){
        return queryFactory
                .selectFrom(account)
                .where(dynamicQuery(firstCondition, secondCondition))
                .fetch();
    }

    private BooleanExpression dynamicQuery(String firstCondition, String secondCondition){
       if(firstCondition.equals("email")){
           return account.email.eq(secondCondition);
       }else if(firstCondition.equals("nickname")){
           return account.nickname.eq(secondCondition);
       }else{
           return null;
       }
    }
}
