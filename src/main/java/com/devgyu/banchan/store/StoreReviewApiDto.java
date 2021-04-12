package com.devgyu.banchan.store;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StoreReviewApiDto {
    private List<String> regDateList = new ArrayList<>();
    private Map<LocalDateTime, StoreAccountReviewDto> accountReviewMap = new HashMap<>();
    private Map<LocalDateTime, String> accountNicknameMap = new HashMap<>();
    private Map<LocalDateTime, String> accountThumbnailMap = new HashMap<>();
    private Map<LocalDateTime, String> storeReviewMap = new HashMap<>();
    private boolean last;

    public StoreReviewApiDto(List<String> regDateList, Map<LocalDateTime, StoreAccountReviewDto> accountReviewMap, Map<LocalDateTime, String> accountNicknameMap, Map<LocalDateTime, String> accountThumbnailMap, Map<LocalDateTime, String> storeReviewMap, boolean last) {
        this.regDateList = regDateList;
        this.accountReviewMap = accountReviewMap;
        this.accountNicknameMap = accountNicknameMap;
        this.accountThumbnailMap = accountThumbnailMap;
        this.storeReviewMap = storeReviewMap;
        this.last = last;
    }
}
