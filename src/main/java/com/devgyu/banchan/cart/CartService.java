package com.devgyu.banchan.cart;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.ItemOptionRepository;
import com.devgyu.banchan.items.ItemRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final AccountRepository accountRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final ItemRepository itemRepository;

    public ResponseEntity<Object> addItem(Account account, CartAddDto cartAddDto) {
        List<CartItem> findCartItemList = cartItemRepository.findAccountCartCartItemStoreFetchByAccountId(account.getId());
        Item newItem = itemRepository.findStoreFetchById(cartAddDto.getItemId());

        Cart cart;
        // 기존에 카트에 상품이 들어있는경우
        if (!findCartItemList.isEmpty()) {
            cart = findCartItemList.get(0).getCart(); // CartItem -> Cart 다대일 관계 -> 어떤 카트상품에서 카트를 추출해도 같은 카트이다.
            StoreOwner findStoreOwner = findCartItemList.get(0).getItem().getStoreOwner();

            // 기존 카트에 담긴 상품의 가게와 새로 추가한 상품의 가게가 일치하는지 확인. 일치하지않으면 기존 장바구니상품 모두 삭제
            StoreOwner addItemStoreOwner = newItem.getStoreOwner();
            if(!findStoreOwner.equals(addItemStoreOwner)){
                findCartItemList.forEach(ci -> cart.removeItem(ci));
            }

            List<Item> itemList = findCartItemList.stream().map(ci -> ci.getItem()).collect(Collectors.toList());
            Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>();
            findCartItemList.forEach(ci -> itemOptionMap.put(ci.getAddDate(), ci.getItemOptionList()));
            List<ItemOption> findItemOptionList =
                    itemOptionRepository.findAllByItemIdAndIdIn(cartAddDto.getItemId(), cartAddDto.getItemOptionIdList());

            // 상품의 옵션 선택했을경우
            if (!cartAddDto.getItemOptionIdList().isEmpty()) {
                Item findItem = findItemOptionList.get(0).getItem(); // ItemOption -> Item = 다대일 관계 -> 어떤 아이템옵션을 가져와도 같은 아이템이다.

                for (CartItem cartItem : findCartItemList) {
                    List<ItemOption> itemOptions = itemOptionMap.get(cartItem.getAddDate()); // 기존 카트에 있던 itemOption리스트
                    if (itemOptions.size() == findItemOptionList.size() && itemOptions.containsAll(findItemOptionList)) {
                        // 새로 장바구니에 추가한 상품이 기존상품이고, 옵션도 똑같이 추가했을경우
                        // 기존에 있던 카트 아이템의 옵션수, 옵션 id, name, price 모두 똑같을경우 같은객체로 보고 카트아이템의 수량 1개 올리고, 가격도 그에 맞게 변경해줌
                        cartItem.addCountForCart();
                        return ResponseEntity.ok().build();
                    }
                }
                CartItem cartItem = new CartItem(cart, findItem, findItemOptionList, 1);
            } else {    // 상품 옵션 선택 안했을경우
                if (itemList.contains(newItem)) {  // 기존 장바구니에 지금 추가한 상품이 있는경우
                    for (CartItem cartItem : findCartItemList) {
                        List<ItemOption> itemOptions = itemOptionMap.get(cartItem.getAddDate());
                        // 장바구니에 담긴 상품중 상품 옵션이 없는 상품 골라내기 + 장바구니에 들어있는 아이템인지 확인
                        if (cartItem.getItem().equals(newItem) && itemOptions.isEmpty()) {
                            cartItem.addCountForCart(); // 장바구니에 들어있는거랑 완전 똑같은 상품인걸 확인했으니 카운트 1개 추가
                            return ResponseEntity.ok().build();
                        }
                    }
                }
                CartItem newCartItem = new CartItem(cart, newItem, 1);
            }
        } else {    // 카트에 상품이 들어있지 않은경우
            cart = accountRepository.findCartFetchById(account.getId()).getCart();
            Long itemId = cartAddDto.getItemId();
            List<Long> itemOptionIdList = cartAddDto.getItemOptionIdList();
            // 옵션 선택 했을경우
            if(!itemOptionIdList.isEmpty()){
                List<ItemOption> selectedItemOptionList = itemOptionRepository.findAllByItemIdAndIdIn(itemId, itemOptionIdList);
                CartItem cartItem = new CartItem(cart, newItem, selectedItemOptionList, 1);
            }else{  // 옵션 선택 안한경우
                CartItem cartItem = new CartItem(cart, newItem, 1);
            }
        }

        return ResponseEntity.ok().build();
    }

    public void deleteCartItem(Account account, Long cartItemId) {
        List<CartItem> cartItemList = cartItemRepository.findAccountCartCartItemStoreFetchByAccountId(account.getId());
        CartItem targetCartItem = cartItemRepository.findById(cartItemId).get();
        Cart cart = cartItemList.get(0).getCart(); // Cart -> CartItem = 일대다 관계 -> 즉, CartItem으로 어떤 Cart를 조회해도 같은 Cart가 나옴
        if(cart.getCartItemList().contains(targetCartItem)){
            cart.removeItem(targetCartItem);
        }
    }
}
