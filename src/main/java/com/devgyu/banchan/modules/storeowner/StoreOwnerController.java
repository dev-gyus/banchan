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
import com.devgyu.banchan.mystore.*;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.orders.OrdersRepository;
import com.devgyu.banchan.orders.OrdersService;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.ReviewDto;
import com.devgyu.banchan.review.ReviewRepository;
import com.devgyu.banchan.review.dto.ReviewFetchDto;
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
    private final OrdersRepository ordersRepository;
    private final OrdersService ordersService;

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
        Page<ReviewFetchDto> findReviews = reviewRepository.findAccountOrdersOrderItemItemStoreByStoreId(storeOwner.getId(), pageable);

        List<ReviewFetchDto> reviewList = findReviews.getContent();
        model.addAttribute("reviewList", reviewList);
        return "mystore/review";
    }

    @PostMapping("/api/{reviewId}/add-store-review")
    @ResponseBody
    public ResponseEntity add_store_review(@CurrentUser StoreOwner storeOwner, @PathVariable Long reviewId,
                                           @Valid @RequestBody ReviewDto reviewDto){

        storeOwnerService.addStoreReview(storeOwner, reviewId, reviewDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order-list/{orderStatus}")
    public String order_list(@CurrentUser StoreOwner storeOwner, @PathVariable String orderStatus,
                             @PageableDefault Pageable pageable, Model model) {
        Page<Orders> orders;
        StoreOwner findStoreOwner;
        if (orderStatus.equals("waiting")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.WAITING, null, null);
        } else if (orderStatus.equals("ready")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.READY, null, null);
        } else if (orderStatus.equals("delivery")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.DELIVERY_START, null, null);
        } else if (orderStatus.equals("completed")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.COMPLETED, null, null);
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        if (!orders.isEmpty()) {
            List<Orders> orderList = orders.getContent();
            Map<Long, Account> orderAccountMap = new HashMap<>();
            Map<Long, List<OrdersItem>> ordersItemMap = new HashMap<>();
            Map<LocalDateTime, Item> itemMap = new HashMap<>();
            Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>(); // 동일한 아이템옵션 Key 중복방지
            for (Orders findOrders : orderList) {
                List<OrdersItem> ordersItemList = findOrders.getOrdersItemList();
                ordersItemMap.put(findOrders.getId(), ordersItemList);
                orderAccountMap.put(findOrders.getId(), findOrders.getAccount());
                for (OrdersItem ordersItem : ordersItemList) {
                    itemMap.put(ordersItem.getAddDate(), ordersItem.getItem());
                    itemOptionMap.put(ordersItem.getAddDate(), ordersItem.getItemOptionList());
                }
            }
            // 주문 가져올때 특정 가게의 Id를 갖고 가져왔고, 하나의 주문은 모두 같은 가게의 상품만 주문 가능하기 때문에 어떤 주문의 아이템을 가져오더라도 같은 가게를 추출함
            findStoreOwner = orders.getContent().get(0).getOrdersItemList().get(0).getItem().getStoreOwner();
            model.addAttribute("storeOwner", findStoreOwner);
            model.addAttribute("hasNext", orders.hasNext());
            model.addAttribute("orderList", orderList);
            model.addAttribute("orderAccountMap", orderAccountMap);
            model.addAttribute("ordersItemMap", ordersItemMap);
            model.addAttribute("itemMap", itemMap);
            model.addAttribute("itemOptionMap", itemOptionMap);
            model.addAttribute("orderStatus", orderStatus);
        } else {
            findStoreOwner = storeOwner;
        }
        model.addAttribute("storeOwner", findStoreOwner);

        return "mystore/order-list";
    }

    @GetMapping("/api/{orderStatus}")
    @ResponseBody
    public MystoreApiDto order_list_api(@CurrentUser StoreOwner storeOwner, @PathVariable String orderStatus,
                                        @PageableDefault Pageable pageable) {
        Page<Orders> orders;
        MystoreApiDto mystoreApiDto;

        if (orderStatus.equals("waiting")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.WAITING, null, null);
        } else if (orderStatus.equals("ready")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.READY, null, null);
        } else if (orderStatus.equals("delivery")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.DELIVERY_START, null, null);
        } else if (orderStatus.equals("completed")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.COMPLETED, null, null);
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        List<Orders> orderList = orders.getContent();
        List<MystoreOrdersDto> mystoreOrdersDtoList = orderList.stream().map(o -> new MystoreOrdersDto(o.getId(), o.getTotalPrice())).collect(Collectors.toList());
        Map<Long, MystoreAccountDto> mystoreAccountDtoMap = new HashMap<>();
        Map<Long, List<MystoreOrderItemDto>> mystoreOrderItemDtoMap = new HashMap<>();
        Map<LocalDateTime, List<MystoreItemOptionDto>> MystoreItemOptionDtoMap = new HashMap<>(); // 동일한 아이템옵션 Key 중복방지
        for (Orders findOrders : orderList) {
            List<OrdersItem> ordersItemList = findOrders.getOrdersItemList();

            List<MystoreOrderItemDto> mystoreOrderItemDtoList = ordersItemList
                    .stream().map(oi -> new MystoreOrderItemDto(oi.getAddDate(), oi.getItem().getName(), oi.getItem().getPrice(), oi.getCount(), oi.getPrice())).collect(Collectors.toList());
            mystoreOrderItemDtoMap.put(findOrders.getId(), mystoreOrderItemDtoList);

            Address accountAddress = findOrders.getAccount().getAddress();
            MystoreAccountDto mystoreAccountDto = new MystoreAccountDto(accountAddress.getJibun(), accountAddress.getRoad(), accountAddress.getDetail()
                    , accountAddress.getExtra(), findOrders.getAccount().getNickname());
            mystoreAccountDtoMap.put(findOrders.getId(), mystoreAccountDto);
            for (OrdersItem ordersItem : ordersItemList) {
                List<MystoreItemOptionDto> mystoreItemOptionDtoList = ordersItem.getItemOptionList()
                        .stream().map(io -> new MystoreItemOptionDto(io.getName(), io.getPrice())).collect(Collectors.toList());
                MystoreItemOptionDtoMap.put(ordersItem.getAddDate(), mystoreItemOptionDtoList);
            }
        }
        // 주문 가져올때 특정 가게의 Id를 갖고 가져왔고, 하나의 주문은 모두 같은 가게의 상품만 주문 가능하기 때문에 어떤 주문의 아이템을 가져오더라도 같은 가게를 추출함
        mystoreApiDto = new MystoreApiDto(mystoreOrdersDtoList, mystoreAccountDtoMap, mystoreOrderItemDtoMap, MystoreItemOptionDtoMap, orders.isLast());

        return mystoreApiDto;
    }

    @PostMapping("/{ordersId}/accept")
    public String orders_accept(@PathVariable Long ordersId) {
        ordersService.acceptOrder(ordersId);
        return "redirect:/orders/order-list/waiting";
    }

    @PostMapping("/{ordersId}/reject")
    public String orders_reject(@PathVariable Long ordersId) {
        ordersService.rejectOrder(ordersId);
        return "redirect:/orders/order-list/waiting";
    }

    @PostMapping("/modify")
    public String myStore_main_do(@CurrentUser StoreOwner storeOwner, @Valid @ModelAttribute MystoreDto mystoreDto,
                                  BindingResult result, Model model){
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
        if(modifyPasswordDto.getPassword().equals("") || !passwordEncoder.matches(modifyPasswordDto.getPassword(), storeOwner.getPassword())){
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
