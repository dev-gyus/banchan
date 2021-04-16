package com.devgyu.banchan.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminStoreApiDto {
    private List<AdminStoreDto> adminStoreDtoList = new ArrayList<>();
    private boolean last;

    public AdminStoreApiDto(List<AdminStoreDto> adminStoreDtoList, boolean last) {
        this.adminStoreDtoList = adminStoreDtoList;
        this.last = last;
    }
}
