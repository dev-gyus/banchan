package com.devgyu.banchan.items;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
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

    public ItemOption(String name, int price, Item item) {
        this.name = name;
        this.price = price;
        this.item = item;
    }

    public ItemOption(Long id, String name, int price, Item item) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.item = item;
    }
}
