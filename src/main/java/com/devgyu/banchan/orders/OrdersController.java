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

    @GetMapping({"", "/"})
    public String orders_main(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model) {
        // 현재 주문내역 가져오기 ( 쿼리 2 + 카운트쿼리 2 + item의 option선택한 order의 갯수(최대 order페이징수만큼 ))
        // 이전 주문내역도 같이 가져오면 페이징이 제대로 처리가 안되므로 따로 가져오도록 한다.
        Page<Orders> notCompOrderList = ordersRepository
                .findAccountItemFetchByIdAndStatus(account.getId(), pageable, OrderStatus.DELIVERY, OrderStatus.READY);
        Page<Orders> compOrderList = ordersRepository
                .findAccountItemFetchByIdAndStatus(account.getId(), pageable, OrderStatus.COMPLETED, null);
        if (notCompOrderList.isEmpty() && compOrderList.isEmpty()) {
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
            return "orders/main";
        }

        List<Orders> orderList = new ArrayList<>();
        orderList.addAll(notCompOrderList.getContent());
        List<Orders> compOrders = compOrderList.getContent();
        orderList.addAll(compOrders);

        Account findAccount = orderList.get(0).getAccount();

        Map<Long, Item> itemMap = new HashMap<>();
        Map<Long, StoreOwner> storeOwnerMap = new HashMap<>();
        Map<Long, List<ItemOption>> itemOptionMap = new HashMap<>();
        Map<Long, Integer> orderPriceMap = new HashMap<>();
        for (Orders orders : orderList) {
            if (orders.getOrderStatus() == OrderStatus.READY || orders.getOrderStatus() == OrderStatus.DELIVERY) {
                List<Long> ordersItemIdList = new ArrayList<>();
                orders.getOrdersItemList().forEach(oi -> {
                    itemMap.put(orders.getId(), oi.getItem());
                    storeOwnerMap.put(orders.getId(), oi.getItem().getStoreOwner());
                    orderPriceMap.put(orders.getId(), oi.getPrice());
                    ordersItemIdList.add(oi.getId());
                });
                List<OrdersItem> findOrdersItem = ordersItemRepository.findItemOptionFetchByIdIn(ordersItemIdList);
                findOrdersItem.forEach(oi -> itemOptionMap.put(orders.getId(), oi.getItemOptionList()));
            }else{
                orders.getOrdersItemList().forEach(oi -> {
                    itemMap.put(orders.getId(), oi.getItem());
                    storeOwnerMap.put(orders.getId(), oi.getItem().getStoreOwner());
                    orderPriceMap.put(orders.getId(), oi.getPrice());
                });
            }
        }
        model.addAttribute("account", findAccount);
        model.addAttribute("notCompOrderList", notCompOrderList);
        model.addAttribute("compOrderList", compOrderList);
        model.addAttribute("itemMap", itemMap);
        model.addAttribute("storeOwnerMap", storeOwnerMap);
        model.addAttribute("orderPriceMap", orderPriceMap);
        model.addAttribute("itemOptionMap", itemOptionMap);

        return "orders/main";
    }


    @PostMapping("/{itemId}/add")
    public String item_selectoption_do(@CurrentUser Account account, @PathVariable Long itemId, @ModelAttribute SelectOptionDto selectOptionDto) {
        ordersService.addOrder(account, itemId, selectOptionDto);

        return "redirect:/orders";
    }
}
