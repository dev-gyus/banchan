package com.devgyu.banchan.mystore;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class MystoreDto {

    private String email;

    @NotBlank
    @Pattern(regexp = "[가-힣]{1}[가-힣A-Za-z0-9]{1,9}",
            message = "첫글자는 한글, 나머지글자는 한글,영어,숫자로 1~10글자이내로 작성해주세요")
    private String name;
    private Address address;
    @NotBlank
    @Pattern(regexp = "\\d{9,11}",
            message = "올바른 가게 전화번호를 입력해 주세요")
    private String tel;
    @NotBlank
    @Pattern(regexp = "\\d{10,11}",
            message = "올바른 휴대폰 번호를 입력해 주세요")
    private String phone;
    @NotBlank
    private String zipcode;
    @NotBlank
    private String road;
    @NotBlank
    private String jibun;
    @NotBlank
    private String detail;
    private String thumbnail;
    private List<String> categories;
    private List<StoreCategory> storeCategoryList;
    @Length(max = 50)
    private String storeIntroduce;

    private boolean managerAuthenticated;
    private boolean haveOrders;



    private String extra;
}
