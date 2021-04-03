package com.devgyu.banchan.items;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SelectOptionDto {
    private Long itemId;
    private String itemName;
    private String thumbnail;
    private int itemPrice;
    private String itemIntroduce;
    private List<Long> optionId = new ArrayList<>();

    private List<SelectOptionListDto> selectOptionListDtoList = new ArrayList<>();

    public void settingParameters(Long itemId, String itemName, String thumbnail, int itemPrice, String itemIntroduce, List<SelectOptionListDto> selectOptionListDtoList) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.thumbnail = thumbnail;
        this.itemPrice = itemPrice;
        this.itemIntroduce = itemIntroduce;
        this.selectOptionListDtoList = selectOptionListDtoList;
    }
}
