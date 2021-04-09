package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.mystore.MystoreDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter @Setter
@DiscriminatorColumn
@NoArgsConstructor
public class StoreOwner extends Account {

    private String tel;
    private String storeIntroduce;

    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Item> items;

    @OneToMany(mappedBy = "storeOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StoreCategory> storeCategories = new ArrayList<>();

    private boolean managerAuthenticated;

    public StoreOwner(String email, String nickname, String password, String name, String phone, Address address, String tel) {
        super(email, nickname, password, name, phone, Roles.ROLE_OWNER, address, UUID.randomUUID().toString());
        this.tel = tel;
    }

    // 실제 가게에 대한 정보가 변경 될경우를 판단하기 위한 비즈니스 메소드
    public boolean isStoreOwnerChanged(MystoreDto mystoreDto){
        if(!this.getNickname().equals(mystoreDto.getName())){
            return true;
        }else if(!this.getName().equals(mystoreDto.getName())){
            return true;
        }else if(!this.getTel().equals(mystoreDto.getTel())){
            return true;
        }else if(!this.getAddress().getZipcode().equals(mystoreDto.getZipcode())){
            return true;
        }else if(!this.getAddress().getRoad().equals(mystoreDto.getRoad())){
            return true;
        }else if(!this.getAddress().getJibun().equals(mystoreDto.getJibun())){
            return true;
        }else if(!this.getAddress().getDetail().equals(mystoreDto.getDetail())){
            return true;
        }
        return false;
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
