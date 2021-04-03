package com.devgyu.banchan.orders;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
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

    public void addOrder(Account account, Long itemId, SelectOptionDto selectOptionDto) {
        Account findAccount = accountRepository.findById(account.getId()).get();
        Orders newOrder;
        if (!selectOptionDto.getOptionId().isEmpty()) { // 옵션을 선택한경우
            List<ItemOption> selectItemOptions = itemOptionRepository.findAllByItemIdAndIdIn(itemId,
                    selectOptionDto.getOptionId().stream().collect(Collectors.toList()));
            Item item = selectItemOptions.get(0).getItem();
            newOrder = new Orders(findAccount);
            OrdersItem ordersItem = new OrdersItem(newOrder, item, selectItemOptions);
        } else {                                                      // 옵션을 선택하지 않은경우
            Item item = itemRepository.findById(itemId).get();
            if (item == null) throw new IllegalArgumentException("잘못된 요청입니다.");
            newOrder = new Orders(findAccount);
            OrdersItem ordersItem = new OrdersItem(newOrder, item);
        }
        ordersRepository.save(newOrder);
    }
}