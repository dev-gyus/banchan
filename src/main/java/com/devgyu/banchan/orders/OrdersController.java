package com.devgyu.banchan.orders;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.items.*;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.ordersitem.OrdersItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrdersController {
    private final AccountRepository accountRepository;
    private final OrdersService ordersService;
    private final OrdersRepository ordersRepository;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final OrdersItemRepository ordersItemRepository;

    @GetMapping({"","/"})
    public String orders_main(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model){
        // 현재 주문내역 가져오기 ( 총 쿼리 2방 )
        // 이전 주문내역도 같이 가져오면 페이징이 제대로 처리가 안되므로 따로 가져오도록 한다.
        Page<Orders> findOrders = ordersRepository
                .findAccountItemFetchByIdAndStatus(account.getId(), pageable, OrderStatus.DELIVERY, OrderStatus.READY);
        if(findOrders.isEmpty()){
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
            return "orders/main";
        }
        List<Orders> ordersList = findOrders.getContent();
        Account findAccount = ordersList.get(0).getAccount();

        Map<Long, Item> itemMap = new HashMap<>();
        Map<Long, StoreOwner> storeOwnerMap = new HashMap<>();
        Map<Long, List<ItemOption>> itemOptionMap = new HashMap<>();
        Map<Long, Integer> orderPriceMap = new HashMap<>();
        for (Orders orders : ordersList) {
            List<Long> ordersItemIdList = new ArrayList<>();
            orders.getOrdersItemList().forEach(oi -> {
                itemMap.put(orders.getId(), oi.getItem());
                storeOwnerMap.put(orders.getId(), oi.getItem().getStoreOwner());
                orderPriceMap.put(orders.getId(), oi.getPrice());
                ordersItemIdList.add(oi.getId());
            });
            List<OrdersItem> findOrdersItem = ordersItemRepository.findItemOptionFetchByIdIn(ordersItemIdList);
            findOrdersItem.forEach(oi -> itemOptionMap.put(orders.getId(), oi.getItemOptionList()));
        }
        model.addAttribute("account", findAccount);
        model.addAttribute("orderList", ordersList);
        model.addAttribute("itemMap", itemMap);
        model.addAttribute("storeOwnerMap", storeOwnerMap);
        model.addAttribute("orderPriceMap", orderPriceMap);
        model.addAttribute("itemOptionMap", itemOptionMap);

        return "orders/main";
    }


    @PostMapping("/{itemId}/add")
    public String item_selectoption_do(@CurrentUser Account account, @PathVariable Long itemId, @ModelAttribute SelectOptionDto selectOptionDto){
        ordersService.addOrder(account, itemId, selectOptionDto);

        return "redirect:/orders";
    }
}
