package com.devgyu.banchan.review;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.orders.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    private String content;
    private int starPoint = 1;
    private LocalDateTime regDate = LocalDateTime.now();
    private ReviewStatus reviewStatus = ReviewStatus.NORMAL;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_review_id")
    private Review storeReview;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "storeReview")
    @JsonIgnore
    private Review customerReview;

    public Review(Account account, Orders orders, String content, int starPoint) {
        this.account = account;
        this.orders = orders;
        this.content = content;
        this.starPoint = starPoint;
        account.getReviewList().add(this);
        orders.getReviewList().add(this);
    }

    public Review(String content, Review customerReview) {
        this.content = content;
        this.customerReview = customerReview;
        customerReview.setStoreReview(this);
    }
}
