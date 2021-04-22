package com.devgyu.banchan.modules.counselor;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CounselorApiDto {
    private List<CounselorDto> counselorDtoList = new ArrayList<>();
    private boolean hasNext;

    public CounselorApiDto(List<CounselorDto> counselorDtoList, boolean hasNext) {
        this.counselorDtoList = counselorDtoList;
        this.hasNext = hasNext;
    }
}
