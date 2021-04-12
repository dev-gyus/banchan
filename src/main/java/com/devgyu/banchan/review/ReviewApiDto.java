package com.devgyu.banchan.review;

import com.devgyu.banchan.modules.storeowner.StoreOwner;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ReviewApiDto {
    private List<Review> reviewList = new ArrayList<>();
    private Map<Long, String> storeNameMap = new HashMap<>();
    private Map<Long, String> storeThumbnailMap = new HashMap<>();
    private Map<Long, Review> storeReviewMap = new HashMap<>();
    private boolean last;

    public ReviewApiDto(List<Review> reviewList, Map<Long, String> storeNameMap, Map<Long, String> storeThumbnailMap,Map<Long, Review> storeReviewMap, boolean last) {
        this.reviewList = reviewList;
        this.storeNameMap = storeNameMap;
        this.storeThumbnailMap = storeThumbnailMap;
        this.storeReviewMap = storeReviewMap;
        this.last = last;
    }
}
