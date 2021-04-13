package com.devgyu.banchan.orders;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderOrders;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.review.Review;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Orders {
    @Id @GeneratedValue
    @Column(name = "orders_id")
    private Long id;
    private LocalDateTime regDate = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "rider_orders_id")
    private RiderOrders riderOrders;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrdersItem> ordersItemList = new ArrayList<>();

    @OneToMany(mappedBy = "orders")
    @JsonIgnore
    private List<Review> reviewList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.WAITING;

    public Orders(Account findAccount) {
        findAccount.addOrder(this);
    }

    private int totalPrice;
    private boolean reviewed;

    public void addItem(OrdersItem ordersItem){
        this.ordersItemList.add(ordersItem);
        ordersItem.setOrders(this);
        this.totalPrice += ordersItem.getPrice();
    }

}
