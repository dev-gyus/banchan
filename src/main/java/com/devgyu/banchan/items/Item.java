package com.devgyu.banchan.items;

import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private String thumbnail;
    private String originThumbnail;
    private int price;
    private boolean soldOut = false;
    private String itemIntroduce;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "items")
    private StoreOwner storeOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemOption> itemOptionList = new ArrayList<>();

    public Item(String name, int price, boolean soldOut, String itemIntroduce, StoreOwner storeOwner, Category category) {
        this.name = name;
        this.price = price;
        this.soldOut = soldOut;
        this.itemIntroduce = itemIntroduce;
        this.storeOwner = storeOwner;
        this.category = category;
        storeOwner.getItems().add(this);
        category.getItems().add(this);
    }
}
