package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreOwner extends Account {

    private String tel;
    private String storeIntroduce;

    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Item> items;

    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StoreCategory> storeCategories = new ArrayList<>();

    public StoreOwner(String email, String nickname, String password, String name, String phone, Address address, String tel) {
        super(email, nickname, password, name, phone, Roles.ROLE_OWNER, address, UUID.randomUUID().toString());
        this.tel = tel;
    }

    public void addStoreCategory(StoreCategory storeCategory){
        storeCategory.setStoreOwner(this);
        storeCategories.add(storeCategory);
    }

    public void addItem(Item item){
        item.setStoreOwner(this);
        items.add(item);
    }
}
