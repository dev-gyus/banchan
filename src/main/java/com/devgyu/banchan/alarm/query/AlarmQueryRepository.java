package com.devgyu.banchan.alarm.query;

import com.devgyu.banchan.alarm.Alarm;
import com.devgyu.banchan.alarm.AlarmStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AlarmQueryRepository {
    Page<Alarm> findByAccountIdAndAlarmStatus(Long id, AlarmStatus alarmStatus, Pageable pageable);

    Long countNewAlarmsByAccountId(Long accountId);
}
