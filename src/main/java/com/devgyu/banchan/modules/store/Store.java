package com.devgyu.banchan.modules.store;

import com.devgyu.banchan.modules.storecategory.StoreCategory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
    @Id @GeneratedValue
    @Column(name = "store_id")
    private Long id;

    private String name;

    public Store(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreCategory> storeCategoryList = new ArrayList<>();

    public void addStoreCategory(StoreCategory storeCategory){
        storeCategory.setStore(this);
        storeCategoryList.add(storeCategory);
    }
}
