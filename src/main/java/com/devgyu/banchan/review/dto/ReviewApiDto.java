package com.devgyu.banchan.review.dto;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.review.dto.ReviewFetchDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ReviewApiDto {
    private List<ReviewFetchDto> reviewFetchDtoList = new ArrayList<>();
    private boolean last;

    public ReviewApiDto(List<ReviewFetchDto> reviewFetchDtoList, boolean last) {
        this.reviewFetchDtoList = reviewFetchDtoList;
        this.last = last;
    }
}
