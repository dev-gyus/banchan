package com.devgyu.banchan.account;

import com.devgyu.banchan.account.query.AccountQueryRepository;
import com.devgyu.banchan.cart.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, Long>, AccountQueryRepository {
    Account findByEmail(String email);

    Account findByName(String name);

}
