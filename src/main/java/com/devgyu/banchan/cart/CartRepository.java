package com.devgyu.banchan.cart;

import com.devgyu.banchan.cart.query.CartQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface CartRepository extends JpaRepository<Cart, Long>, CartQueryRepository {
}
