package com.devgyu.banchan.orders;

import lombok.Data;

@Data
public class CompOrderItemDto {
    private String thumbnail;
    private String name;
    private int count;

    public CompOrderItemDto(String thumbnail, String name, int count) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.count = count;
    }
}
