package com.devgyu.banchan.review;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.orders.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final AccountRepository accountRepository;
    private final OrdersRepository ordersRepository;
    private final AppProperties appProperties;

    public void addReview(Long accountId, Long orderId, ReviewDto reviewDto) {
        Account findAccount = accountRepository.findById(accountId).get();
        Orders findOrders = ordersRepository.findById(orderId).get();
        Review newReview = new Review(findAccount, findOrders, reviewDto.getContent(), reviewDto.getStarPoint());
        reviewRepository.save(newReview);
        findOrders.setReviewed(true);
    }

    public String addImage(MultipartFile image) throws IOException {
        String uploadFolderPrefix = appProperties.getUploadFolderPrefix();
        File folder = new File(uploadFolderPrefix + "/review/");
        if(!folder.exists()){
            folder.mkdirs();
        }
        String extension = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String convertedFileName = UUID.randomUUID().toString() + "_" + LocalDateTime.now() + "." + extension;

        File settings = new File(folder, convertedFileName);
        image.transferTo(settings);

        String url = appProperties.getHost() + "/upload/review/" + convertedFileName;
        return url;
    }
}
