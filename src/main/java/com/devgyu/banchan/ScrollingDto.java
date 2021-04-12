package com.devgyu.banchan;

import com.devgyu.banchan.storelist.StoreListDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ScrollingDto {
    private List<StoreListDto> storeList = new ArrayList<>();
    private boolean last;

    public ScrollingDto(List<StoreListDto> storeList, boolean last) {
        this.storeList = storeList;
        this.last = last;
    }
}
