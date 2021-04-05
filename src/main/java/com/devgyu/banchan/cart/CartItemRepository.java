package com.devgyu.banchan.cart;

import com.devgyu.banchan.cart.query.CartItemQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemQueryRepository {
}
