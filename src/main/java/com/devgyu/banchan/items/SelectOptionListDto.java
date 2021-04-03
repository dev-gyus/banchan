package com.devgyu.banchan.items;

import lombok.Data;

@Data
public class SelectOptionListDto {
    private Long optionId;
    private String optionName;
    private int optionPrice;

    public SelectOptionListDto(Long optionId, String optionName, int optionPrice) {
        this.optionId = optionId;
        this.optionName = optionName;
        this.optionPrice = optionPrice;
    }
}
