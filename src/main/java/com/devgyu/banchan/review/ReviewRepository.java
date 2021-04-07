package com.devgyu.banchan.review;

import com.devgyu.banchan.review.query.ReviewQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryRepository {
}
