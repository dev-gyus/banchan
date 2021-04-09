package com.devgyu.banchan.orders;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.cart.CartItem;
import com.devgyu.banchan.cart.CartItemRepository;
import com.devgyu.banchan.items.*;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.ordersitem.OrdersItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdersService {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final AccountRepository accountRepository;
    private final OrdersRepository ordersRepository;
    private final OrdersItemRepository ordersItemRepository;
    private final CartItemRepository cartItemRepository;

    public void addOrder(Account account, Long itemId, SelectOptionDto selectOptionDto) { // 상품페이지에서 바로 주문하는경우
        Account findAccount = accountRepository.findById(account.getId()).get();
        Orders newOrder;
        if (!selectOptionDto.getOptionId().isEmpty()) { // 옵션을 선택한경우
            List<ItemOption> selectItemOptions = itemOptionRepository.findAllByItemIdAndIdInStoreAuthTrue(itemId,
                    selectOptionDto.getOptionId().stream().collect(Collectors.toList()));
            if(selectItemOptions.isEmpty()) throw new IllegalArgumentException("상품이 존재하지 않거나, 현재 준비중인 가게 입니다.");
            Item item = selectItemOptions.get(0).getItem();
            newOrder = new Orders(findAccount);
            OrdersItem ordersItem = new OrdersItem(newOrder, item, selectItemOptions, 1);
        } else {                                                      // 옵션을 선택하지 않은경우
            Item item = itemRepository.findItemOptionStoreAuthTrueFetchById(itemId);
            if (item == null) throw new IllegalArgumentException("상품이 존재하지 않거나, 현재 준비중인 가게 입니다.");
            newOrder = new Orders(findAccount);
            OrdersItem ordersItem = new OrdersItem(newOrder, item, 1);
        }
        ordersRepository.save(newOrder);
    }

    public void addCartOrder(Long accountId) {
        List<CartItem> cartItemList = cartItemRepository.findAccountCartCartItemStoreAuthTrueFetchByAccountId(accountId);
        Cart findCart = cartItemList.get(0).getCart();
        if(cartItemList.isEmpty()){
            throw new IllegalArgumentException("잘못된 요청 입니다.");
        }
        Account findAccount = cartItemList.get(0).getCart().getAccount();
        Orders orders = new Orders(findAccount);

        for (CartItem cartItem : cartItemList) {
            Item findItem = cartItem.getItem();
            if(cartItem.getItemOptionList().isEmpty()){ // 해당 장바구니 내의 상품의 상품옵션이 선택되어있는경우
                OrdersItem ordersItem = new OrdersItem(orders, findItem, cartItem.getCount());
                findCart.removeItem(cartItem);
            }else{                                      // 해당 장바구니 내의 상품의 상품옵션이 돼있지 않은경우
                List<ItemOption> itemOptionList = cartItem.getItemOptionList();
                OrdersItem ordersItem = new OrdersItem(orders, findItem, itemOptionList, cartItem.getCount());
                findCart.removeItem(cartItem);
            }
        }
    }
    public void cancelOrder(Long orderId){
        // 주문 취소는 Soft Delete방식 채용
        Orders orders = ordersRepository.findById(orderId).get();
        orders.setOrderStatus(OrderStatus.CANCELED);
    }

    public void acceptOrder(Long ordersId) {
        List<Orders> findOrders = ordersRepository.findAccountFetchById(ordersId);
        if(findOrders.isEmpty()){
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        Orders orders = findOrders.get(0);
        orders.setOrderStatus(OrderStatus.READY);
        // TODO 주문한 고객에게 알림 보낼것
    }

    public void rejectOrder(Long ordersId) {
        List<Orders> findOrders = ordersRepository.findAccountFetchById(ordersId);
        if(findOrders.isEmpty()){
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        Orders orders = findOrders.get(0);
        orders.setOrderStatus(OrderStatus.REJECTED);
        // TODO 주문한 고객에게 알림 보낼것
    }
}
