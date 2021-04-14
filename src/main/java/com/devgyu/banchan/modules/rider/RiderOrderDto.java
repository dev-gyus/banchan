package com.devgyu.banchan.modules.rider;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RiderOrderDto {
    private Long orderId;
    private String accountRoad;
    private String accountDetail;
    private String storeNickname;
    private String storeRoad;
    private String storeDetail;
    private LocalDateTime orderRegDate;

    public RiderOrderDto(Long orderId, String accountRoad,
                         String accountDetail, String storeNickname, String storeRoad,
                         String storeDetail, LocalDateTime orderRegDate) {
        this.orderId = orderId;
        this.accountRoad = accountRoad;
        this.accountDetail = accountDetail;
        this.storeNickname = storeNickname;
        this.storeRoad = storeRoad;
        this.storeDetail = storeDetail;
        this.orderRegDate = orderRegDate;
    }
}
