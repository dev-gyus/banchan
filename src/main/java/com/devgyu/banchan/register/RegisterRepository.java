package com.devgyu.banchan.register;

import com.devgyu.banchan.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface RegisterRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);


    boolean existsByEmailOrNickname(String email, String nickname);
}
