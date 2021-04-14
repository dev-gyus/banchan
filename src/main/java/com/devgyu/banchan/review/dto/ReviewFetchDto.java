package com.devgyu.banchan.review.dto;

import com.devgyu.banchan.account.Roles;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewFetchDto {
    private Long reviewId;
    private Long storeId;
    private LocalDateTime reviewRegDate;
    private String reviewContent;
    private int starPoint;
    private String accountThumbnail;
    private String accountName;
    private Roles accountRole;
    private String storeNickname;
    private String storeThumbnail;
    private String storeReviewContent;

    public ReviewFetchDto(Long reviewId, Long storeId, LocalDateTime reviewRegDate, String reviewContent, int starPoint, String accountThumbnail, String accountName, Roles accountRole, String storeNickname, String storeThumbnail, String storeReviewContent) {
        this.reviewId = reviewId;
        this.storeId = storeId;
        this.reviewRegDate = reviewRegDate;
        this.reviewContent = reviewContent;
        this.starPoint = starPoint;
        this.accountThumbnail = accountThumbnail;
        this.accountName = accountName;
        this.accountRole = accountRole;
        this.storeNickname = storeNickname;
        this.storeThumbnail = storeThumbnail;
        this.storeReviewContent = storeReviewContent;
    }
}
