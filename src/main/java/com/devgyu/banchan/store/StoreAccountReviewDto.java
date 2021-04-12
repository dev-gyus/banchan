package com.devgyu.banchan.store;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StoreAccountReviewDto {
    private int starPoint;
    private String content;

    public StoreAccountReviewDto(int starPoint, String content) {
        this.starPoint = starPoint;
        this.content = content;
    }
}
