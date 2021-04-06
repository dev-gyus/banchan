package com.devgyu.banchan.cart.query;

import com.devgyu.banchan.cart.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CartItemQueryRepository {
    Page<CartItem> findAccountCartItemItemOptionFetchByAccountId(Long id, Pageable pageable);

    List<CartItem> findAccountCartCartItemStoreFetchByAccountId(Long accountId);
}
