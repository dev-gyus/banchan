package com.devgyu.banchan.alarm.query;

import com.devgyu.banchan.account.QAccount;
import com.devgyu.banchan.alarm.Alarm;
import com.devgyu.banchan.alarm.AlarmStatus;
import com.devgyu.banchan.alarm.QAlarm;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

import static com.devgyu.banchan.account.QAccount.account;
import static com.devgyu.banchan.alarm.QAlarm.alarm;

public class AlarmRepositoryImpl implements AlarmQueryRepository{
    private EntityManager em;
    private JPAQueryFactory queryFactory;

    public AlarmRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Alarm> findByAccountIdAndAlarmStatus(Long id, AlarmStatus alarmStatus, Pageable pageable) {
        QueryResults<Alarm> result = queryFactory
                .selectFrom(alarm)
                .join(alarm.account, account)
                .where(account.id.eq(id).and(alarm.alarmStatus.eq(alarmStatus)))
                .orderBy(alarm.alertDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Long countNewAlarmsByAccountId(Long accountId) {
        return queryFactory
                .selectFrom(alarm)
                .join(alarm.account, account)
                .where(account.id.eq(accountId).and(alarm.alarmStatus.eq(AlarmStatus.ALERT)))
                .fetchCount();
    }
}
