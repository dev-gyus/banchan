package com.devgyu.banchan.mystore;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MystoreOrderItemDto {
    private LocalDateTime addDate;
    private String itemName;
    private int itemPrice;
    private int count;
    private int price;

    public MystoreOrderItemDto(LocalDateTime addDate, String itemName, int itemPrice, int count, int price) {
        this.addDate = addDate;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.price = price;
    }
}
