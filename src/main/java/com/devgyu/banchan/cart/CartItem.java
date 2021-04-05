package com.devgyu.banchan.cart;

import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
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
public class CartItem {
    @Id @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private List<ItemOption> itemOptionList;

    private int price;
    private int count;

    private LocalDateTime addDate = LocalDateTime.now();

    public CartItem(Cart cart, Item item, List<ItemOption> itemOptionList, int count) {
        this.cart = cart;
        this.item = item;
        this.itemOptionList = itemOptionList;
        this.price = (item.getPrice() * count);
        for (ItemOption itemOption : itemOptionList) {
            this.price += itemOption.getPrice();
            itemOption.getCartItemList().add(this);
        }
        this.count = count;
        cart.addItem(this);

    }
    public CartItem(Cart cart, Item item, int count) {
        this.cart = cart;
        cart.getCartItemList().add(this);
        this.item = item;
        this.price = (item.getPrice() * count);
        this.count = count;
        cart.addItem(this);
    }

    public void addCountForCart(){
        count++;
        price = (item.getPrice() * count);
        for (ItemOption itemOption : itemOptionList) {
            price += (itemOption.getPrice() * count);
        }
        this.cart.addItem(this);
    }
}
