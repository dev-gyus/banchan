package com.devgyu.banchan.items;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;


@Data
public class AddItemDto {
    private Long id;
    @NotBlank
    @Pattern(regexp = "[가-힣a-zA-Z0-9()]{2,10}",
            message = "한글, 숫자, 영문대,소문자, (, )를 이용해 2~10글자 내로 작성해주세요")
    private String name;
    private String thumbnail;
    @Min(value = 1L, message = "최소 1이상 입력해주세요")
    private int price;
    @NotBlank
    @Length(min = 1, max = 50)
    private String itemIntroduce;
    private String category;
    private List<ItemOptionDto> itemOptionDtoList = new ArrayList<>();
    private List<ItemOption> itemOptionList = new ArrayList<>();
    private MultipartFile thumbnailFile;

    public void setParameter(Long id, @NotBlank @Pattern(regexp = "[가-힣a-zA-Z0-9()]{2,10}",
            message = "한글, 숫자, 영문대,소문자, (, )를 이용해 2~10글자 내로 작성해주세요") String name, String thumbnail, @Min(value = 1L, message = "최소 1이상 입력해주세요") int price, @NotBlank @Length(min = 1, max = 50) String itemIntroduce) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.itemIntroduce = itemIntroduce;
    }
}
