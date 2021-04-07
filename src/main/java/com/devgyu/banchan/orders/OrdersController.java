package com.devgyu.banchan.orders;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.customer.Customer;
import com.devgyu.banchan.cart.CartItem;
import com.devgyu.banchan.cart.CartItemRepository;
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

import java.time.LocalDateTime;
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
    private final CartItemRepository cartItemRepository;

    @GetMapping({"", "/"})
    public String orders_main(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model) {
        // 현재 주문내역 가져오기 ( 쿼리 2 + 카운트쿼리 2 + item의 option선택한 order의 갯수(최대 order페이징수만큼 ))
        // 이전 주문내역도 같이 가져오면 페이징이 제대로 처리가 안되므로 따로 가져오도록 한다.
        Page<Orders> notCompOrderList = ordersRepository
                .findAccountItemFetchByIdAndStatus(account.getId(), pageable,
                        OrderStatus.DELIVERY_READY, OrderStatus.DELIVERY_START, OrderStatus.READY, OrderStatus.WAITING);
        Page<Orders> compOrderList = ordersRepository
                .findAccountItemFetchByIdAndStatus(account.getId(), pageable,
                        OrderStatus.COMPLETED, null, null, null);
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

        Map<Long, List<OrdersItem>> ordersItemMap = new HashMap<>();
        Map<Long, Item> itemMap = new HashMap<>();
        Map<Long, StoreOwner> storeOwnerMap = new HashMap<>();
        Map<Long, List<ItemOption>> itemOptionMap = new HashMap<>();
        Map<Long, Integer> orderPriceMap = new HashMap<>();
        for (Orders orders : orderList) {
            ordersItemMap.put(orders.getId(), orders.getOrdersItemList());
            if (orders.getOrderStatus() == OrderStatus.READY ||
                    orders.getOrderStatus() == OrderStatus.DELIVERY_START ||
                    orders.getOrderStatus() == OrderStatus.WAITING) {
                List<Long> ordersItemIdList = new ArrayList<>();
                // 하나의 주문은 하나의 가게에서만 할수있음
                // 따라서 주문 상품목록의 상품들의 가게는 어떤걸 가져와도 똑같음
                storeOwnerMap.put(orders.getId(), orders.getOrdersItemList().get(0).getItem().getStoreOwner());
                orders.getOrdersItemList().forEach(oi -> {
                    itemMap.put(oi.getId(), oi.getItem());
                    orderPriceMap.put(oi.getId(), oi.getPrice());
                    ordersItemIdList.add(oi.getId());
                });
                // ItemOption을 별도로 가져오는이유 = 위의 Order조회할때 이미 OrderItemList로 컬렉션을 조회함
                // 여기서 OrderItemList의 ItemOptionList를 또 조회하면 이중 컬렉션 조회가 되기때문에 쿼리 분리함
                // Order를 조회하는 이유 = 여러개의 Order를 페이징 처리해서 가져오기위함
                List<OrdersItem> findOrdersItem = ordersItemRepository.findItemOptionFetchByIdIn(ordersItemIdList);
                findOrdersItem.forEach(oi -> itemOptionMap.put(oi.getId(), oi.getItemOptionList()));
            }else{
                // 하나의 주문은 하나의 가게에서만 할수있음
                // 따라서 주문 상품목록의 상품들의 가게는 어떤걸 가져와도 똑같음
                storeOwnerMap.put(orders.getId(), orders.getOrdersItemList().get(0).getItem().getStoreOwner());
                ordersItemMap.put(orders.getId(), orders.getOrdersItemList());
                orders.getOrdersItemList().forEach(oi -> {
                    itemMap.put(oi.getId(), oi.getItem());
                    orderPriceMap.put(oi.getId(), oi.getPrice());
                });
            }
        }
        model.addAttribute("account", findAccount);
        model.addAttribute("notCompOrderList", notCompOrderList);
        model.addAttribute("compOrderList", compOrderList);
        model.addAttribute("ordersItemMap", ordersItemMap);
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

    @PostMapping("/cart-order")
    public String cart_order(@CurrentUser Account account){
        ordersService.addCartOrder(account.getId());
        return "redirect:/orders";
    }

    @PostMapping("/{orderId}/cancel")
    public String order_cancel(@PathVariable Long orderId){
        ordersService.cancelOrder(orderId);
        return "redirect:/orders";
    }

    @GetMapping("/order-list/{orderStatus}")
    public String order_list(@CurrentUser StoreOwner storeOwner, @PathVariable String orderStatus,
                             @PageableDefault Pageable pageable,Model model){
        Page<Orders> orders;
        if(orderStatus.equals("waiting")) {
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.WAITING, null, null);
        }else if(orderStatus.equals("ready")){
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.READY, null, null);
        }else if(orderStatus.equals("delivery")){
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.DELIVERY_START, null, null);
        }else if(orderStatus.equals("completed")){
            orders = ordersRepository.findAccountItemFetchByStoreIdAndStatus(storeOwner.getId(),
                    pageable, OrderStatus.COMPLETED, null, null);
        }else{
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        if(!orders.isEmpty()) {
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
            model.addAttribute("hasNext", orders.hasNext());
            model.addAttribute("orderList", orderList);
            model.addAttribute("orderAccountMap", orderAccountMap);
            model.addAttribute("ordersItemMap", ordersItemMap);
            model.addAttribute("itemMap", itemMap);
            model.addAttribute("itemOptionMap", itemOptionMap);
            model.addAttribute("orderStatus", orderStatus);
        }
        return "mystore/order-list";
    }
    @PostMapping("/{ordersId}/accept")
    public String orders_accept(@PathVariable Long ordersId){
        ordersService.acceptOrder(ordersId);
        return "redirect:/orders/order-list/waiting";
    }

    @PostMapping("/{ordersId}/reject")
    public String orders_reject(@PathVariable Long ordersId){
        ordersService.rejectOrder(ordersId);
        return "redirect:/orders/order-list/waiting";
    }
}
