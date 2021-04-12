package com.devgyu.banchan.mystore;

import lombok.Data;

@Data
public class MystoreOrdersDto {
    private Long id;
    private int totalPrice;

    public MystoreOrdersDto(Long id, int totalPrice) {
        this.id = id;
        this.totalPrice = totalPrice;
    }
}
