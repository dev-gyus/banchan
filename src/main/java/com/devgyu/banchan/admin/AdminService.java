package com.devgyu.banchan.admin;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.UserAccount;
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
}
