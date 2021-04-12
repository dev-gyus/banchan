package com.devgyu.banchan.mystore;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MystoreItemOptionDto {
    private String name;
    private int price;

    public MystoreItemOptionDto(String name, int price) {
        this.name = name;
        this.price = price;
    }
}
