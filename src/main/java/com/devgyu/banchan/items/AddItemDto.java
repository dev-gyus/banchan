package com.devgyu.banchan.items;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;


@Data
public class AddItemDto {
    private Long id;
    private String name;
    private MultipartFile thumbnailFile;
    private String thumbnail;
    private int price;
    private String itemIntroduce;
    private String category;
    private List<ItemOption> itemOptionList = new ArrayList<>();
}
