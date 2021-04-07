package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.ItemOptionRepository;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.mystore.CategoryWrapper;
import com.devgyu.banchan.mystore.MystoreDto;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.ReviewDto;
import com.devgyu.banchan.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mystore")
public class StoreOwnerController {
    private final ReviewRepository reviewRepository;
    private final StoreOwnerRepository ownerRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final StoreOwnerService ownerService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final StoreOwnerService storeOwnerService;

    @GetMapping({"", "/"})
    public String myStore_main(@CurrentUser StoreOwner storeOwner, @ModelAttribute MystoreDto mystoreDto,
                               Model model){
        StoreOwner findOwner = ownerRepository.findCategoriesFetchById(storeOwner.getId()).get(0);
        List<String> categories;
        if(findOwner.getStoreCategories() != null) {
            categories = findOwner.getStoreCategories()
                    .stream().map(sc -> sc.getCategory().getName()).collect(Collectors.toList());
        }else{
            categories = Collections.emptyList();
        }
        Address address = findOwner.getAddress();
        modelMapper.map(findOwner, mystoreDto);
        modelMapper.map(address, mystoreDto);
        mystoreDto.setCategories(categories);
        return "mystore/main";
    }
    @GetMapping("/review")
    public String mystore_review(@CurrentUser StoreOwner storeOwner, @ModelAttribute ReviewDto reviewDto, @PageableDefault Pageable pageable, Model model){
        Page<Review> findReviews = reviewRepository.findAccountOrdersOrderItemItemStoreByStoreId(storeOwner.getId(), pageable);
        List<Review> reviewList = findReviews.getContent();
        Map<LocalDateTime, Review> storeReviewMap = new HashMap<>();
        Map<LocalDateTime, Account> accountMap = new HashMap<>();
        Map<LocalDateTime, List<OrdersItem>> ordersItemMap = new HashMap<>();
        Map<LocalDateTime, Item> itemMap = new HashMap<>();
        Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>();

        for (Review review : reviewList) {
            List<OrdersItem> ordersItemList = review.getOrders().getOrdersItemList();
            ordersItemMap.put(review.getRegDate(), ordersItemList);
            accountMap.put(review.getRegDate(), review.getAccount());
            storeReviewMap.put(review.getRegDate(), review.getStoreReview());

            for (OrdersItem ordersItem : ordersItemList) {
                itemMap.put(ordersItem.getAddDate(), ordersItem.getItem());

                List<Long> itemOptionList = ordersItem.getItemOptionList().stream().map(io -> io.getId()).collect(Collectors.toList());
                List<ItemOption> findItemOptionList = itemOptionRepository.findAllByItemIdAndIdIn(ordersItem.getItem().getId(), itemOptionList);
                itemOptionMap.put(ordersItem.getAddDate(), findItemOptionList);
            }
        }
        model.addAttribute("reviewList", reviewList);
        model.addAttribute("storeReviewMap", storeReviewMap);
        model.addAttribute("accountMap", accountMap);
        model.addAttribute("itemMap", itemMap);
        model.addAttribute("ordersItemMap", ordersItemMap);
        model.addAttribute("itemOptionMap", itemOptionMap);

        return "mystore/review";
    }

    @PostMapping("/{reviewId}/add-store-review")
    @ResponseBody
    public ResponseEntity add_store_review(@CurrentUser StoreOwner storeOwner, @PathVariable Long reviewId,
                                           @Valid @RequestBody ReviewDto reviewDto){

        storeOwnerService.addStoreReview(storeOwner, reviewId, reviewDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/modify")
    public String myStore_main_do(@CurrentUser StoreOwner storeOwner, @Valid @ModelAttribute MystoreDto mystoreDto,
                                  BindingResult result, Model model){
        // TODO 가게소개(StoreIntroduce)도 수정할 수 있도록 변경
        if(result.hasErrors()){
            return "mystore/main";
        }
        ownerService.modifyStoreOwner(storeOwner, mystoreDto);
        return "redirect:/mystore";
    }

    @GetMapping("/modify-password")
    public String modifyPassword(@CurrentUser StoreOwner storeOwner, @ModelAttribute ModifyPasswordDto modifyPasswordDto){

        return "mystore/change-password";
    }

    @PostMapping("/modify-password")
    public String modifyPassword_do(@CurrentUser StoreOwner storeOwner, @Valid @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                  BindingResult result, Model model){
        StoreOwner findOwner = ownerRepository.findById(storeOwner.getId()).get();
        if(modifyPasswordDto.getPassword().equals("") || !passwordEncoder.matches(modifyPasswordDto.getPassword(), findOwner.getPassword())){
            result.rejectValue("password", null, "인증에 실패 하였습니다.");
            return "mystore/change-password";
        }
        return "mystore/change-password-main";
    }
    @PostMapping("/modify-password-main")
    public String modifyPassword_main(@CurrentUser StoreOwner storeOwner, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                      BindingResult result) throws IllegalAccessException {
        if(modifyPasswordDto.getPasswordRepeat().equals("")){
            result.rejectValue("passwordRepeat",null,"비밀번호 확인은 필수입니다.");
            return "mystore/change-password-main";
        }else if(modifyPasswordDto.getPassword().equals("")){
            result.rejectValue("password",null,"변경할 비밀번호를 입력해주세요.");
            return "mystore/change-password-main";
        }else if(!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat",null,"비밀번호가 일치하지 않습니다.");
            return "mystore/change-password-main";
        }
        ownerService.modifyPassword(storeOwner.getEmail(), modifyPasswordDto);
        return "redirect:/mystore";
    }
}
