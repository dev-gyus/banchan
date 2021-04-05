package com.devgyu.banchan.cart.query;

import com.devgyu.banchan.cart.Cart;

import java.util.List;

public interface CartQueryRepository {
    List<Cart> existByAccountId(Long accountId);
}
