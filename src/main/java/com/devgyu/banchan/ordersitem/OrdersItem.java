package com.devgyu.banchan.ordersitem;

import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.orders.Orders;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
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

    @OneToMany
    @JsonIgnore
    private List<ItemOption> itemOptionList = new ArrayList<>();

    private int price;

    public OrdersItem(Orders orders, Item item, List<ItemOption> itemOptionList) {
        this.orders = orders;
        orders.getOrdersItemList().add(this);
        this.item = item;
        this.itemOptionList = itemOptionList;
        this.price += item.getPrice();
        itemOptionList.forEach(io -> this.price += io.getPrice());
    }

    public OrdersItem(Orders orders, Item item) {
        this.orders = orders;
        this.item = item;
        this.price += item.getPrice();
    }
}
