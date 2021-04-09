package com.devgyu.banchan.storelist;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Data
public class StoreListDto {
    private Long id;
    private String name;
    private String thumbnail;
    private boolean managerAuthenticated;

    public StoreListDto(Long id, String name, String thumbnail, boolean managerAuthenticated) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.managerAuthenticated = managerAuthenticated;
    }
}
