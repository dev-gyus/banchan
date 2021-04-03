package com.devgyu.banchan.orders;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.ordersitem.OrdersItem;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<OrdersItem> ordersItemList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.READY;

    public Orders(Account findAccount) {
        this.account = findAccount;
    }

    private void addItem(OrdersItem ordersItem){
        this.ordersItemList.add(ordersItem);
    }
}
