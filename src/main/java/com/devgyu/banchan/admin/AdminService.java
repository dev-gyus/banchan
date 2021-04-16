package com.devgyu.banchan.admin;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.UserAccount;
import com.devgyu.banchan.alarm.Alarm;
import com.devgyu.banchan.alarm.AlarmType;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderRepository;
import com.devgyu.banchan.modules.rider.query.RiderRepositoryImpl;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {
    private final AccountRepository accountRepository;
    private final SessionRegistry sessionRegistry;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RiderRepository riderRepository;

    public Account userBlock(Long accountId, String blockReason) {
        Account findAccount = accountRepository.findById(accountId).get();

        if(!findAccount.isBlocked()){
            // 계정 정지
            findAccount.setBlocked(true);
            findAccount.addBlockCount();
            findAccount.setBlockReason(blockReason);
            findAccount.setBlockedDate(LocalDateTime.now());

            // 강제 로그아웃 기능
            List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
            for (Object allPrincipal : allPrincipals) {
                UserAccount principal = (UserAccount) allPrincipal;
                if(findAccount.getId() == principal.getId()){
                    List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
                    for (SessionInformation session : allSessions) {
                        session.expireNow();
                    }
                }
            }
        }
        return findAccount;
    }

    public Account userUnBlock(Long accountId) {
        Account findAccount = accountRepository.findById(accountId).get();
        if(findAccount.isBlocked()){
            findAccount.setBlocked(false);
            findAccount.setUnblockedDate(LocalDateTime.now());
        }
        return findAccount;
    }

    public void storeAccept(Long storeId) {
        StoreOwner storeOwner = storeOwnerRepository.findById(storeId).get();
        storeOwner.setManagerAuthenticated(true);
        storeOwner.setLastAuthDate(LocalDateTime.now());
        String content = "가게 영업승인이 완료되었습니다!";
        new Alarm(storeOwner, content, AlarmType.STORE_ACCEPTED, LocalDateTime.now());
    }

    public void storeReject(Long storeId, String rejectReason) {
        StoreOwner storeOwner = storeOwnerRepository.findById(storeId).get();
        storeOwner.setManagerAuthenticated(false);
        storeOwner.setLastAuthDate(LocalDateTime.now());
        String content = "반려사유: " + rejectReason;
        new Alarm(storeOwner, content, AlarmType.STORE_REJECTED, LocalDateTime.now());
    }

    public void riderAccept(Long riderId) {
        Rider rider = riderRepository.findById(riderId).get();
        rider.setManagerAuthenticated(true);
        rider.setLastAuthDate(LocalDateTime.now());
        String content = "라이더 권한 승인이 완료되었습니다!";
        new Alarm(rider, content, AlarmType.RIDER_ACCEPTED, LocalDateTime.now());
    }

    public void riderReject(Long storeId, String rejectReason) {
        Rider rider = riderRepository.findById(storeId).get();
        rider.setManagerAuthenticated(false);
        rider.setLastAuthDate(LocalDateTime.now());
        String content = "반려사유: " + rejectReason;
        new Alarm(rider, content, AlarmType.RIDER_REJECTED, LocalDateTime.now());
    }
}
