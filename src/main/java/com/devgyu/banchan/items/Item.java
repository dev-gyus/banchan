package com.devgyu.banchan.items;

import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @JoinColumn(name = "storeOwner_id")
    private StoreOwner storeOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemOption> itemOptionList = new ArrayList<>();

    public Item(String name, int price, String itemIntroduce,
                StoreOwner storeOwner, Category category, List<ItemOption> itemOptionList) {
        this.name = name;
        this.price = price;
        this.itemIntroduce = itemIntroduce;
        this.storeOwner = storeOwner;
        this.category = category;
        this.itemOptionList = itemOptionList;
    }

    public void addItemOption(ItemOption itemOption) {
        this.itemOptionList.add(itemOption);
        itemOption.setItem(this);
    }

    public void removeItemOptions(List<ItemOption> itemItemOptionList) {
        this.itemOptionList.removeAll(itemOptionList);
    }
}