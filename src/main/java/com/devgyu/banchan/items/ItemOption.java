package com.devgyu.banchan.items;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@EqualsAndHashCode(of = "id")
public class ItemOption {
    @Id @GeneratedValue
    @Column(name = "item_option_id")
    private Long id;

    private String name;
    private int price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemOptionList")
    private Item item;
}
