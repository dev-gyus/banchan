package com.devgyu.banchan.account.query;

import com.devgyu.banchan.account.Account;

public interface AccountQueryRepository {
    Account findCartFetchById(Long id);
}
