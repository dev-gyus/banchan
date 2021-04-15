package com.devgyu.banchan.orders;

import lombok.Data;

@Data
public class OrdersFetchDto {
    private OrderStatus orderStatus;
    private String orderDate;
    private boolean reviewed;
    private int orderPrice;
    private Long ordersId;

    public OrdersFetchDto(OrderStatus orderStatus, String orderDate, boolean reviewed, int orderPrice, Long ordersId) {
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.reviewed = reviewed;
        this.orderPrice = orderPrice;
        this.ordersId = ordersId;
    }
}
