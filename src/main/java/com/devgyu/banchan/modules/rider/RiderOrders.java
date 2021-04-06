package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.orders.Orders;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RiderOrders {
    @Id @GeneratedValue
    @Column(name = "rider_orders_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Rider rider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    private Orders orders;

    private LocalDateTime regDate = LocalDateTime.now();

    private int deliveryPrice = 3000;

    public RiderOrders(Rider rider, Orders orders, int deliveryPrice) {
        this.rider = rider;
        this.orders = orders;
        this.deliveryPrice = deliveryPrice;
        rider.getRiderOrdersList().add(this);
        orders.setRiderOrders(this);
    }

    public RiderOrders(Rider rider, Orders orders) {
        this.rider = rider;
        this.orders = orders;
        rider.getRiderOrdersList().add(this);
        orders.setRiderOrders(this);
    }
}
