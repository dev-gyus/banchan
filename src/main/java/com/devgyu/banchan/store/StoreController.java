package com.devgyu.banchan.store;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.items.*;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.mystore.MystoreDto;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.ReviewRepository;
import com.devgyu.banchan.review.dto.ReviewApiDto;
import com.devgyu.banchan.review.dto.ReviewFetchDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreOwnerRepository storeOwnerRepository;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final ItemOptionRepository itemOptionRepository;

    @GetMapping({"/{storeId}"})
    public String store_main(@PathVariable Long storeId, Model model) {
        List<Category> categoryList = categoryRepository.findAllByStoreOwnerId(storeId);
        StoreOwner findOwner = categoryList.get(0).getStoreCategoryList().get(0).getStoreOwner();
        Address address = findOwner.getAddress();
        StoreDto map = modelMapper.map(findOwner, StoreDto.class);
        modelMapper.map(address, map);

        List<String> categoryNames = categoryList.stream().map(c -> c.getName()).collect(Collectors.toList());

        Map<String, List<Item>> itemMap = new HashMap<>();
        for (String categoryName : categoryNames) {
            List<Item> findItemList = itemRepository.findAllByCategoryAndStore(categoryName, storeId);
            itemMap.put(categoryName, findItemList);
        }
        model.addAttribute("itemMap", itemMap);
        model.addAttribute(map);
        return "store/main";
    }

    @GetMapping("/{itemId}/selectoption")
    public String item_selectoption(@PathVariable Long itemId, @ModelAttribute SelectOptionDto selectOptionDto, Model model) {
        Item findItem = itemRepository.findItemOptionStoreAuthTrueFetchById(itemId);
        if (findItem == null || !findItem.getStoreOwner().isManagerAuthenticated())
            throw new IllegalArgumentException("상품이 존재하지 않거나, 현재 준비중인 가게 입니다.");

        List<SelectOptionListDto> convertedOptionList = findItem.getItemOptionList()
                .stream().map(io -> new SelectOptionListDto(io.getId(), io.getName(), io.getPrice())).collect(Collectors.toList());

        selectOptionDto.settingParameters(findItem.getId(), findItem.getName(), findItem.getThumbnail(),
                findItem.getPrice(), findItem.getItemIntroduce(), convertedOptionList, findItem.getStoreOwner().isManagerAuthenticated());

        model.addAttribute(selectOptionDto);
        return "store/select-option";
    }

    @GetMapping({"/{storeId}/review"})
    public String store_review(@PathVariable Long storeId, @PageableDefault Pageable pageable, Model model) {
        Page<ReviewFetchDto> findReviews = reviewRepository.findAccountOrdersOrderItemItemStoreByStoreId(storeId, pageable);

        List<ReviewFetchDto> reviewList = findReviews.getContent();
        model.addAttribute("reviewList", reviewList);

        return "store/review";
    }

    @GetMapping({"/api/{storeId}/review"})
    @ResponseBody
    public ReviewApiDto api_store_review(@PathVariable Long storeId, @PageableDefault Pageable pageable, Model model) {
        Page<ReviewFetchDto> findReviews = reviewRepository.findAccountOrdersOrderItemItemStoreByStoreId(storeId, pageable);

        ReviewApiDto reviewApiDto = new ReviewApiDto(findReviews.getContent(), findReviews.isLast());
        return reviewApiDto;
    }

}
