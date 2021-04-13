package com.devgyu.banchan.admin.query;

import com.devgyu.banchan.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminQueryRepository {
    List<Account> findAccountByConditions(String firstCondition, String secondCondition);
}
