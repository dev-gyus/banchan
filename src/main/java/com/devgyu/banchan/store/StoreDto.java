package com.devgyu.banchan.store;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class StoreDto {
    private String email;
    private String name;
    private Address address;
    private String tel;
    private String phone;
    private String zipcode;
    private String road;
    private String jibun;
    private String detail;
    private String thumbnail;
    private List<String> categories;
    private List<StoreCategory> storeCategoryList;
    private String storeIntroduce;



    private String extra;
}
