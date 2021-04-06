package com.devgyu.banchan.ordersitem;

import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.orders.Orders;
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
public class OrdersItem {
    @Id @GeneratedValue
    @Column(name = "orders_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ItemOption> itemOptionList = new ArrayList<>();

    private int price;
    private int count;
    private LocalDateTime addDate = LocalDateTime.now();

    public OrdersItem(Orders orders, Item item, List<ItemOption> itemOptionList, int count) {
        this.orders = orders;
        this.item = item;
        this.itemOptionList.addAll(itemOptionList);
        this.price = (item.getPrice() * count);
        itemOptionList.forEach(io -> this.price += (io.getPrice() * count));
        this.count = count;
        orders.addItem(this);
    }

    public OrdersItem(Orders orders, Item item, int count) {
        this.orders = orders;
        this.item = item;
        this.price = (item.getPrice() * count);
        this.count = count;
        orders.addItem(this);
    }
}
