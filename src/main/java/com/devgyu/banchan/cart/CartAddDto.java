package com.devgyu.banchan.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartAddDto {
    private Long itemId;
    private List<Long> itemOptionIdList;
}
