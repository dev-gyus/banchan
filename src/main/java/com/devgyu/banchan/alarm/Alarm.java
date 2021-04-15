package com.devgyu.banchan.alarm;

import com.devgyu.banchan.account.Account;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Alarm {
    @Id @GeneratedValue
    @Column(name = "alarm_id")
    private Long id;
    private String content;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    private LocalDateTime alertDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private AlarmStatus alarmStatus = AlarmStatus.ALERT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public Alarm(Account account, String content, AlarmType alarmType, LocalDateTime alertDate) {
        this.account = account;
        this.content = content;
        this.alarmType = alarmType;
        this.alertDate = alertDate;
        account.getAlarmList().add(this);
    }
}
