package com.devgyu.banchan.orders;

import lombok.Data;

@Data
public class NotCompOrderItemDto {
    private Long ordersItemId;
    private String itemThumbnail;
    private String itemName;
    private int itemPrice;
    private int count;
    private int totalPrice;

    public NotCompOrderItemDto(Long ordersItemId, String itemThumbnail, String itemName, int itemPrice, int count, int totalPrice) {
        this.ordersItemId = ordersItemId;
        this.itemThumbnail = itemThumbnail;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.count = count;
        this.totalPrice = totalPrice;
    }
}
