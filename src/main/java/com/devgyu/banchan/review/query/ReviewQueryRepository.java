package com.devgyu.banchan.review.query;

import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.dto.ReviewFetchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewQueryRepository {


    Page<ReviewFetchDto> findAccountOrdersOrderItemItemStoreByStoreId(Long storeId, Pageable pageable);

    Page<ReviewFetchDto> findStoreReviewAccountOrdersOrderItemItemStoreLeftByAccountId(Long accountId, Pageable pageable);
}
