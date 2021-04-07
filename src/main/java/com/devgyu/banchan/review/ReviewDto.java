package com.devgyu.banchan.review;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Data
public class ReviewDto {
    private Long id;
    @NotBlank
    private String content;
    private int starPoint;

}
