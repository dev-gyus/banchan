package com.devgyu.banchan.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    public Page<Alarm> findAllAlertAlarms(Long id, Pageable pageable) {
        Page<Alarm> alarmList = alarmRepository.findByAccountIdAndAlarmStatus(id, AlarmStatus.ALERT, pageable);
        // 알람 페이지에 진입하면 확인해야할 알람은 모두 확인한알람으로 변경한다
        if(!alarmList.isEmpty()) {
            alarmList.forEach(a -> {
                if (a.getAlarmStatus() == AlarmStatus.ALERT) {
                    a.setAlarmStatus(AlarmStatus.CHECKED);
                }
            });
        }
        return alarmList;
    }

    public Page<Alarm> findAllCheckedAlarms(Long id, Pageable pageable) {
        Page<Alarm> alarmList = alarmRepository.findByAccountIdAndAlarmStatus(id, AlarmStatus.CHECKED, pageable);
        return alarmList;
    }
}
