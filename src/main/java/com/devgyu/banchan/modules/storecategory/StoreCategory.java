package com.devgyu.banchan.modules.storecategory;

import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.store.Store;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreCategory {
    @Id @GeneratedValue
    @Column(name = "store_category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public StoreCategory(Store store, Category category) {
        this.store = store;
        this.category = category;
    }
}
