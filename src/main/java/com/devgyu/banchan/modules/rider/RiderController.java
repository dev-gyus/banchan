package com.devgyu.banchan.modules.rider;

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
import java.util.stream.Stream;

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
        Rider findRider = riderRepository.findById(rider.getId()).get();
        Address address = findRider.getAddress();
        modelMapper.map(findRider, riderMainDto);
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
    public String order_list(@CurrentUser Rider rider, @PageableDefault Pageable pageable, Model model){
        Rider findRider = riderRepository.findById(rider.getId()).get();
            String road = findRider.getAddress().getRoad();
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
            Page<Orders> findOrders = ordersRepository.findAccountItemFetchByStoreRoadIdAndStatus(convertedRoad, OrderStatus.READY, pageable);
            List<Orders> orderList = findOrders.getContent();
            Map<Long, Address> storeAddressMap = new HashMap<>();
            Map<Long, StoreOwner> storeMap = new HashMap<>();
            for (Orders order : orderList) {
                // 상품 주문할때 항상 동일한 가게의 상품만 주문할 수 있기 때문에 같은 주문이라면 주문한 상품 어떤것이든 동일한 가게가 추출됨
                StoreOwner storeOwner = order.getOrdersItemList().get(0).getItem().getStoreOwner();
                Address storeAddress = storeOwner.getAddress();

                storeMap.put(order.getId(), storeOwner);
                storeAddressMap.put(order.getId(), storeAddress);
            }
            model.addAttribute("orderStatus", "ready");
            model.addAttribute("orderList", orderList);
            model.addAttribute("storeMap", storeMap);
            model.addAttribute("storeAddressMap", storeAddressMap);
        return "rider/order-list";
    }

    @PostMapping("/order-accept/{ordersId}")
    public String addOrders(@CurrentUser Rider rider, @PathVariable Long ordersId){
        riderService.addOrders(rider, ordersId);
        return "redirect:/rider/order-list/delivery";
    }

    @GetMapping("/order-list/{orderStatus}")
    public String findStatusOrder(@CurrentUser Rider rider, @PathVariable String orderStatus, Pageable pageable,Model model){

        if(orderStatus.equals("delivery")) {
            Page<RiderOrders> riderOrders = riderOrdersRepository.findOrdersItemStoreFetchByRiderIdAndStatus(rider.getId(), OrderStatus.DELIVERY, pageable);
            Map<LocalDateTime, OrdersItem> ordersItemMap = new HashMap<>();
            Map<LocalDateTime, Item> itemMap = new HashMap<>();
            Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>();

            List<Orders> ordersList = riderOrders.getContent().stream().map(ro -> ro.getOrders()).collect(Collectors.toList());
            for (Orders orders : ordersList) {
                List<OrdersItem> ordersItemList = orders.getOrdersItemList();
                for (OrdersItem ordersItem : ordersItemList) {

                    ordersItemMap.put(ordersItem.getAddDate(), ordersItem);

                    itemMap.put(ordersItem.getAddDate(), ordersItem.getItem());
                    List<Long> itemOptionIdList = ordersItem.getItemOptionList().stream().map(io -> io.getId()).collect(Collectors.toList());

                    List<ItemOption> findItemOptionList = itemOptionRepository.findAllByItemIdAndIdIn(ordersItem.getItem().getId(), itemOptionIdList);
                    itemOptionMap.put(ordersItem.getAddDate(), findItemOptionList);
                }
            }
            model.addAttribute("ordersList", ordersList);
            model.addAttribute("ordersItemMap", ordersItemMap);
            model.addAttribute("itemMap", itemMap);
            model.addAttribute("itemOptionMap", itemOptionMap);

        }else if(orderStatus.equals("completed")){
            Page<RiderOrders> findRiderOrders = riderOrdersRepository.findOrdersItemStoreFetchByRiderIdAndStatus(rider.getId(), OrderStatus.COMPLETED, pageable);
            List<RiderOrders> riderOrdersList = findRiderOrders.getContent();
            Map<Long, StoreOwner> storeMap = new HashMap<>();
            Map<Long, Orders> ordersMap = new HashMap<>();
            for (RiderOrders riderOrders : riderOrdersList) {

                Orders orders = riderOrders.getOrders();    // RiderOrders -> orders 1:1관계
                ordersMap.put(riderOrders.getId(), orders);

                StoreOwner storeOwner = orders.getOrdersItemList().get(0).getItem().getStoreOwner(); // 특정 주문의 상품은 모두 같은 가게의 상품임
                storeMap.put(riderOrders.getId(), storeOwner);
            }
            model.addAttribute("riderOrdersList",riderOrdersList);
            model.addAttribute("storeMap",storeMap);
            model.addAttribute("ordersMap",ordersMap);

        }else{

            throw new IllegalArgumentException("잘못된 요청입니다.");

        }

        model.addAttribute("orderStatus", orderStatus);
        return "rider/order-list-status";
    }

    @GetMapping("/modify-password")
    public String modifyPassword(@CurrentUser Rider rider, @ModelAttribute ModifyPasswordDto modifyPasswordDto){

        return "rider/change-password";
    }
    @PostMapping("/modify-password")
    public String modifyPassword_do(@CurrentUser Rider rider, @Valid @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                    BindingResult result, Model model){
        Rider findRider = riderRepository.findById(rider.getId()).get();
        if(modifyPasswordDto.getPassword().equals("") || !passwordEncoder.matches(modifyPasswordDto.getPassword(), findRider.getPassword())){
            result.rejectValue("password", null, "인증에 실패 하였습니다.");
            return "rider/change-password";
        }
        return "rider/change-password-main";
    }
    @PostMapping("/modify-password-main")
    public String modifyPassword_main(@CurrentUser Rider rider, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                      BindingResult result) throws IllegalAccessException {
        if(modifyPasswordDto.getPasswordRepeat().equals("")){
            result.rejectValue("passwordRepeat",null,"비밀번호 확인은 필수입니다.");
            return "rider/change-password-main";
        }else if(modifyPasswordDto.getPassword().equals("")){
            result.rejectValue("password",null,"변경할 비밀번호를 입력해주세요.");
            return "rider/change-password-main";
        }else if(!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat",null,"비밀번호가 일치하지 않습니다.");
            return "rider/change-password-main";
        }
        riderService.modifyPassword(rider.getEmail(), modifyPasswordDto);
        return "redirect:/rider/rider-page";
    }
}
