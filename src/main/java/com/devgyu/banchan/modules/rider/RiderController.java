package com.devgyu.banchan.modules.rider;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.ItemOptionRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.orders.OrderStatus;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.orders.OrdersRepository;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.ordersitem.OrdersItemRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rider")
@RequiredArgsConstructor
public class RiderController {
    private final RiderRepository riderRepository;
    private final OrdersItemRepository ordersItemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final RiderOrdersRepository riderOrdersRepository;
    private final OrdersRepository ordersRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RiderService riderService;



    @GetMapping("/rider-page")
    public String rider_page(@CurrentUser Rider rider, @ModelAttribute RiderMainDto riderMainDto) {
        Address address = rider.getAddress();
        modelMapper.map(rider, riderMainDto);
        modelMapper.map(address, riderMainDto);
        return "rider/main";
    }

    @PostMapping("/modify")
    public String myStore_main_do(@CurrentUser Rider rider, @Valid @ModelAttribute RiderMainDto riderMainDto,
                                  BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rider/main";
        }
        riderService.modifyRider(rider, riderMainDto);
        return "redirect:/rider/rider-page";
    }

    @GetMapping("/order-list")
    public String orderList(@CurrentUser Rider rider, @PageableDefault Pageable pageable, Model model) {
        if(!rider.isManagerAuthenticated()){
            model.addAttribute(rider);
            model.addAttribute("orderStatus", "ready");
            return "rider/order-list";
        }

        String road = rider.getAddress().getRoad();
        String gu = road.split(" ")[1];
        String convertedRoad = "";

        if (gu.indexOf('시') != -1) {
            convertedRoad = gu.split("시")[0] + "시";
        } else if (gu.indexOf('군') != -1) {
            convertedRoad = gu.split("군")[0] + "군";
        } else if (gu.indexOf('구') != -1) {
            convertedRoad = gu.split("구")[0] + "구";
        } else {
            throw new IllegalArgumentException("주문을 확인하던중 에러가 발생하였습니다. 관리자에게 문의 부탁드립니다.");
        }

        Page<RiderOrderDto> findOrders = riderRepository.findAccountItemFetchByStoreRoadIdAndStatus(convertedRoad, OrderStatus.READY, pageable);
        List<RiderOrderDto> orderList = findOrders.getContent();

        model.addAttribute("orderStatus", "ready");
        model.addAttribute("orderList", orderList);

        return "rider/order-list";
    }
    // rider/order-list page scroll method
    @GetMapping("/api/order-list")
    @ResponseBody
    public RiderWaitingApiDto api_orderList(@CurrentUser Rider rider, @PageableDefault Pageable pageable) {

        String road = rider.getAddress().getRoad();
        String gu = road.split(" ")[1];
        String convertedRoad = "";

        if (gu.indexOf('시') != -1) {
            convertedRoad = gu.split("시")[0] + "시";
        } else if (gu.indexOf('군') != -1) {
            convertedRoad = gu.split("군")[0] + "군";
        } else if (gu.indexOf('구') != -1) {
            convertedRoad = gu.split("구")[0] + "구";
        } else {
            throw new IllegalArgumentException("주문을 확인하던중 에러가 발생하였습니다. 관리자에게 문의 부탁드립니다.");
        }

        Page<RiderOrderDto> findOrders = riderRepository.findAccountItemFetchByStoreRoadIdAndStatus(convertedRoad, OrderStatus.READY, pageable);
        List<RiderOrderDto> orderList = findOrders.getContent();

        return new RiderWaitingApiDto(orderList, findOrders.isLast());
    }

    @PostMapping("/order-accept/{ordersId}")
    public String addOrders(@CurrentUser Rider rider, @PathVariable Long ordersId) {
        riderService.addOrders(rider, ordersId);
        return "redirect:/rider/order-list/delivery_ready";
    }

    @GetMapping("/order-list/{orderStatus}")
    public String findStatusOrder(@CurrentUser Rider rider, @PathVariable String orderStatus, @PageableDefault Pageable pageable, Model model) {
        if (orderStatus.equals("delivery_ready")) {
            deliveryMethod(rider, pageable, OrderStatus.DELIVERY_READY, model);
        } else if (orderStatus.equals("delivery_start")) {
            deliveryMethod(rider, pageable, OrderStatus.DELIVERY_START, model);
        } else if (orderStatus.equals("completed")) {
            deliveryMethod(rider, pageable, OrderStatus.COMPLETED, model);
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        model.addAttribute(rider);
        model.addAttribute("orderStatus", orderStatus);
        return "rider/order-list-status";
    }
    private void deliveryMethod(Rider rider, Pageable pageable, OrderStatus orderStatus, Model model) {
        Page<RiderOrderDto> findRiderOrders = riderRepository.findAccountItemFetchByRiderIdAndStatus(rider.getId(), orderStatus, pageable);
        List<RiderOrderDto> ordersList = findRiderOrders.getContent();

        model.addAttribute("ordersList", ordersList);
    }

    // rider/order-list-status page scroll method
    @GetMapping("/api/order-list/{orderStatus}")
    @ResponseBody
    public RiderNotWaitingApiDto api_findStatusOrder(@CurrentUser Rider rider, @PathVariable String orderStatus, @PageableDefault Pageable pageable) {
        RiderNotWaitingApiDto riderNotWaitingApiDto;
        if (orderStatus.equals("delivery_ready")) {
            riderNotWaitingApiDto = api_deliveryMethod(rider, OrderStatus.DELIVERY_READY, pageable);
        } else if (orderStatus.equals("delivery_start")) {
            riderNotWaitingApiDto = api_deliveryMethod(rider, OrderStatus.DELIVERY_START, pageable);
        } else if (orderStatus.equals("completed")) {
            riderNotWaitingApiDto = api_deliveryMethod(rider, OrderStatus.COMPLETED, pageable);
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        return riderNotWaitingApiDto;
    }
    // api_findStatusOrder subMethod
    private RiderNotWaitingApiDto api_deliveryMethod(Rider rider, OrderStatus orderStatus, Pageable pageable) {
        Page<RiderOrderDto> findRiderOrders = riderRepository.findAccountItemFetchByRiderIdAndStatus(rider.getId(), orderStatus, pageable);
        List<RiderOrderDto> ordersList = findRiderOrders.getContent();

        return new RiderNotWaitingApiDto(ordersList, findRiderOrders.isLast());
    }



    @GetMapping("/{orderId}/detail")
    public String delivery_detail(@CurrentUser Rider rider, @PathVariable Long orderId, Model model) {
        List<RiderOrders> riderOrders = riderOrdersRepository.findAccountOrdersItemStoreFetchByOrdersIdAndRiderId(orderId, rider.getId());
        if (riderOrders.isEmpty()) {
            throw new IllegalArgumentException("잘못된 요청입니다");
        }
        Orders orders = riderOrders.get(0).getOrders(); // orderId로 조회한거라 오더는 무조건 한 개임
        StoreOwner storeOwner = orders.getOrdersItemList().get(0).getItem().getStoreOwner();// 하나의 주문의 상품은 모두 같은 가게의 상품임
        Account account = orders.getAccount();

        Map<LocalDateTime, Item> itemMap = new HashMap<>();
        Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>();

        List<OrdersItem> ordersItemList = orders.getOrdersItemList();
        for (OrdersItem ordersItem : ordersItemList) {

            itemMap.put(ordersItem.getAddDate(), ordersItem.getItem());
            List<Long> itemOptionIdList = ordersItem.getItemOptionList().stream().map(io -> io.getId()).collect(Collectors.toList());

            List<ItemOption> findItemOptionList = itemOptionRepository.findAllByItemIdAndIdIn(ordersItem.getItem().getId(), itemOptionIdList);
            itemOptionMap.put(ordersItem.getAddDate(), findItemOptionList);
        }
        model.addAttribute("orderStatus", "delivery_ready");
        model.addAttribute("account", account);
        model.addAttribute("storeOwner", storeOwner);
        model.addAttribute("order", orders);
        model.addAttribute("ordersItemList", ordersItemList);
        model.addAttribute("itemMap", itemMap);
        model.addAttribute("itemOptionMap", itemOptionMap);
        return "rider/delivery-detail";
    }

    @GetMapping("/auth-waiting")
    public String auth_waiting(){
        return "rider/auth-waiting";
    }

    @PostMapping("/{orderId}/delivery-start")
    public String accept_delivery(@CurrentUser Rider rider, @PathVariable Long orderId) {
        riderService.startDelivery(rider, orderId);
        return "redirect:/rider/order-list/delivery_start";
    }
    @PostMapping("/{orderId}/delivery-completed")
    public String completed_delivery(@CurrentUser Rider rider, @PathVariable Long orderId) {
        riderService.completedDelivery(orderId);
        return "redirect:/rider/order-list/completed";
    }

    @GetMapping("/{orderId}/navigation")
    public String rider_navigation(@CurrentUser Rider rider, @PathVariable Long orderId){
        return "rider/navigation";
    }

    @GetMapping("/modify-password")
    public String modifyPassword(@CurrentUser Rider rider, @ModelAttribute ModifyPasswordDto modifyPasswordDto) {

        return "rider/change-password";
    }

    @PostMapping("/modify-password")
    public String modifyPassword_do(@CurrentUser Rider rider, @Valid @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                    BindingResult result, Model model) {
        if (modifyPasswordDto.getPassword().equals("") || !passwordEncoder.matches(modifyPasswordDto.getPassword(), rider.getPassword())) {
            result.rejectValue("password", null, "인증에 실패 하였습니다.");
            return "rider/change-password";
        }
        return "rider/change-password-main";
    }

    @PostMapping("/modify-password-main")
    public String modifyPassword_main(@CurrentUser Rider rider, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                      BindingResult result) throws IllegalAccessException {
        if (modifyPasswordDto.getPasswordRepeat().equals("")) {
            result.rejectValue("passwordRepeat", null, "비밀번호 확인은 필수입니다.");
            return "rider/change-password-main";
        } else if (modifyPasswordDto.getPassword().equals("")) {
            result.rejectValue("password", null, "변경할 비밀번호를 입력해주세요.");
            return "rider/change-password-main";
        } else if (!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())) {
            result.rejectValue("passwordRepeat", null, "비밀번호가 일치하지 않습니다.");
            return "rider/change-password-main";
        }
        riderService.modifyPassword(rider.getEmail(), modifyPasswordDto);
        return "redirect:/rider/rider-page";
    }
}
