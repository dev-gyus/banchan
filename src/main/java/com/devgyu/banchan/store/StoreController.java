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
        Page<Review> findReviews = reviewRepository.findAccountOrdersOrderItemItemStoreByStoreId(storeId, pageable);
        List<Review> reviewList = findReviews.getContent();
        Map<LocalDateTime, Review> storeReviewMap = new HashMap<>();
        Map<LocalDateTime, Account> accountMap = new HashMap<>();

        for (Review review : reviewList) {
            accountMap.put(review.getRegDate(), review.getAccount());
            storeReviewMap.put(review.getRegDate(), review.getStoreReview());
        }
        model.addAttribute("storeId", storeId);
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("storeReviewMap", storeReviewMap);
        model.addAttribute("accountMap", accountMap);

        return "store/review";
    }

    @GetMapping({"/api/{storeId}/review"})
    @ResponseBody
    public StoreReviewApiDto api_store_review(@PathVariable Long storeId, @PageableDefault Pageable pageable, Model model) {
        Page<Review> findReviews = reviewRepository.findAccountOrdersOrderItemItemStoreByStoreId(storeId, pageable);
        List<Review> reviewList = findReviews.getContent();
        List<String> regDateList = new ArrayList<>();
        Map<LocalDateTime, StoreAccountReviewDto> accountReviewMap = new HashMap<>();
        Map<LocalDateTime, String> accountNicknameMap = new HashMap<>();
        Map<LocalDateTime, String> accountThumbnailMap = new HashMap<>();
        Map<LocalDateTime, String> storeReviewContentMap = new HashMap<>();

        for (Review review : reviewList) {
            // List<LocalDateTime> 타입으로 Jackson을 이용해 json으로 변환하면 LocalDateTime포맷으로 변환함
            // -> 맨 뒤 의미없는 0이 사라짐, 하지만 Map의 Key로 LocalDateTime을 주면 String형으로 변환되므로 실제 뷰페이지에서 키값이 다름
            // 결국 같은 키이지만 변환되는 타입이 다르기때문에 뷰페이지에서 undifined나옴. -> 그냥 String으로 변환해서 쓰면 문제없음
            regDateList.add(review.getRegDate().toString());
            accountReviewMap.put(review.getRegDate(), new StoreAccountReviewDto(review.getStarPoint(), review.getContent()));
            accountNicknameMap.put(review.getRegDate(), review.getAccount().getNickname());
            accountThumbnailMap.put(review.getRegDate(), review.getAccount().getThumbnail());
            if(review.getStoreReview() != null) {
                storeReviewContentMap.put(review.getRegDate(), review.getStoreReview().getContent());
            }
        }

        StoreReviewApiDto storeReviewApiDto =
                new StoreReviewApiDto(regDateList, accountReviewMap, accountNicknameMap, accountThumbnailMap, storeReviewContentMap, findReviews.isLast());
        return storeReviewApiDto;
    }

}
