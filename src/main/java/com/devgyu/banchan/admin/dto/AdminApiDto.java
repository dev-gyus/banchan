package com.devgyu.banchan.admin.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminApiDto {
    private List<AdminBlockDto> adminBlockDtoList = new ArrayList<>();
    private boolean last;

    public AdminApiDto(List<AdminBlockDto> adminBlockDtoList, boolean last) {
        this.adminBlockDtoList = adminBlockDtoList;
        this.last = last;
    }
}
