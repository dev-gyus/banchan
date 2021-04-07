package com.devgyu.banchan.review;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.orders.Orders;
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

    public Review(Account account, Orders orders, String content, int starPoint) {
        this.account = account;
        this.orders = orders;
        this.content = content;
        this.starPoint = starPoint;
        account.getReviewList().add(this);
        orders.getReviewList().add(this);
    }
}
