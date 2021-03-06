package com.devgyu.banchan.review;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.Roles;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.ordersitem.OrdersItemRepository;
import com.devgyu.banchan.review.dto.ReviewApiDto;
import com.devgyu.banchan.review.dto.ReviewFetchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final OrdersItemRepository ordersItemRepository;

    @GetMapping({"","/"})
    public String review_main(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model){
        Page<ReviewFetchDto> findReviews = reviewRepository.findStoreReviewAccountOrdersOrderItemItemStoreLeftByAccountId(account.getId(), pageable);
        List<ReviewFetchDto> reviewList = findReviews.getContent();
        Roles accountRole = account.getRole();
        if(reviewList.isEmpty()){
            model.addAttribute("accountRole", accountRole);
            return "review/main";
        }
        Long storeId = reviewList.get(0).getStoreId();

        model.addAttribute("accountRole", accountRole);
        model.addAttribute("storeId", storeId);
        model.addAttribute("reviewList", reviewList);
        return "review/main";
    }

    @GetMapping("/api")
    @ResponseBody
    public ReviewApiDto review_api(@CurrentUser Account account, @PageableDefault Pageable pageable){
        Page<ReviewFetchDto> findReviews = reviewRepository.findStoreReviewAccountOrdersOrderItemItemStoreLeftByAccountId(account.getId(), pageable);

        return new ReviewApiDto(findReviews.getContent(), findReviews.isLast());
    }

    @GetMapping("/{orderId}/add")
    public String review_add(@CurrentUser Account account, @ModelAttribute ReviewDto reviewDto,
                             @PathVariable Long orderId, Model model) {
        // ????????? ????????? ?????? ????????? ???????????? ????????? ????????? ordersItem?????? ????????? = ???????????? ?????? / ?????? ?????? ??????
        List<OrdersItem> ordersItemList = ordersItemRepository.findOrdersItemItemOptionStoreOwnerByOrderId(orderId);
        if(ordersItemList.isEmpty()){
            throw new IllegalArgumentException("????????? ???????????????.");
        }
        Orders orders = ordersItemList.get(0).getOrders();  // Orders -> OrdersItem = ????????? ?????? -> ?????? ??????????????? ???????????? ?????? ??????
        // ????????? ????????? ????????? ??????????????? ?????? Item -> StoreOwner = ????????? ?????? -> ?????? ??????????????? ?????? ????????? ???????????? ?????? ??????
        StoreOwner storeOwner = ordersItemList.get(0).getItem().getStoreOwner();

        Map<LocalDateTime, Item> itemMap = new HashMap<>();
        Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>();
        for (OrdersItem ordersItem : ordersItemList) {
            itemMap.put(ordersItem.getAddDate(), ordersItem.getItem());
            itemOptionMap.put(ordersItem.getAddDate(), ordersItem.getItemOptionList());
        }

        model.addAttribute("ordersItemList", ordersItemList);
        model.addAttribute("orders", orders);
        model.addAttribute("storeOwner", storeOwner);
        model.addAttribute("itemMap", itemMap);
        model.addAttribute("itemOptionMap", itemOptionMap);
        return "review/add";
    }

    @PostMapping("/api/image")
    @ResponseBody
    public String add_image(@RequestBody MultipartFile image) throws IOException {
        String image_url = reviewService.addImage(image);
        return image_url;
    }
    @PostMapping("/{orderId}/add")
    public String review_add(@CurrentUser Account account, @ModelAttribute ReviewDto reviewDto,
                             @PathVariable Long orderId){
        reviewService.addReview(account.getId(), orderId, reviewDto);
        return "redirect:/orders";
    }
}
