package com.devgyu.banchan.items;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = {"id", "name", "price"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemOption {
    @Id @GeneratedValue
    @Column(name = "item_option_id")
    private Long id;

    private String name;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // 상품 수정페이지에서 아이템 옵션 추가했을경우
    public ItemOption(String name, int price, Item item) {
        this.name = name;
        this.price = price;
        this.item = item;
        item.getItemOptionList().add(this);
    }

    // 아이템옵션 조회하는경우
    public ItemOption(Long id, String name, int price, Item item) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.item = item;
    }
}
