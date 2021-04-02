package com.devgyu.banchan.items;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemOptionDto {
    private Long id;
    private String name;
    private int price;

    public ItemOptionDto(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
