package com.devgyu.banchan.orders;

import lombok.Data;

@Data
public class OrdersItemOptionDto {
    private String name;
    private int price;

    public OrdersItemOptionDto(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
