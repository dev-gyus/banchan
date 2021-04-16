package com.devgyu.banchan.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminRiderApiDto {
    private List<AdminRiderDto> adminRiderDtoList = new ArrayList<>();
    private boolean last;

    public AdminRiderApiDto(List<AdminRiderDto> adminRiderDtoList, boolean last) {
        this.adminRiderDtoList = adminRiderDtoList;
        this.last = last;
    }
}
