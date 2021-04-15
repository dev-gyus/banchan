package com.devgyu.banchan.alarm;

import com.devgyu.banchan.alarm.query.AlarmQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmQueryRepository {
}
