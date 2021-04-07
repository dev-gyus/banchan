package com.devgyu.banchan.review.query;

import com.devgyu.banchan.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewQueryRepository {
    public Page<Review> findAccountOrdersOrderItemItemStoreByAccountId(Long accountId, Pageable pageable);

    public Page<Review> findAccountOrdersOrderItemItemStoreByStoreId(Long storeId, Pageable pageable);
}
