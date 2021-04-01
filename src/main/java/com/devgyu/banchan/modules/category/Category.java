package com.devgyu.banchan.modules.category;

import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {
    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;
    private String name;
    private String urlName;

    public Category(String name, String urlName) {
        this.name = name;
        this.urlName = urlName;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<StoreCategory> storeCategoryList = new ArrayList<>();


    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Item> items = new ArrayList<>();

    public void addStoreCategory(StoreCategory storeCategory){
        storeCategoryList.add(storeCategory);
        storeCategory.setCategory(this);
    }
    public void addItem(Item item){
        items.add(item);
        item.setCategory(this);
    }
}
