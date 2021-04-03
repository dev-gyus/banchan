package com.devgyu.banchan.orders;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersMain {
    private List<Orders> notCompleteOrders = new ArrayList<>();
    private List<Orders> completedOrders = new ArrayList<>();

    public OrdersMain(List<Orders> notCompleteOrders, List<Orders> completedOrders) {
        this.notCompleteOrders = notCompleteOrders;
        this.completedOrders = completedOrders;
    }
}
