package com.devgyu.banchan.cart;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.items.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CartItem> cartItemList = new ArrayList<>();

    private int totalCartPrice;

    public Cart(Account account, CartItem cartItem) {
        this.account = account;
        this.cartItemList.add(cartItem);
        this.totalCartPrice += cartItem.getPrice();
    }
    public Cart(Account account) {
        this.account = account;
        account.setCart(this);
    }

    public void addItem(CartItem cartItem){
        this.cartItemList.add(cartItem);
        this.totalCartPrice += cartItem.getPrice();
    }
}
