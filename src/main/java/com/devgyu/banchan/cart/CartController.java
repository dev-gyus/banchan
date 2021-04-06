package com.devgyu.banchan.cart;

import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.ItemOptionRepository;
import com.devgyu.banchan.items.ItemRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartService cartService;
    private final AccountRepository accountRepository;
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;

    @GetMapping({"","/"})
    public String cart_main(@CurrentUser Account account, @PageableDefault Pageable pageable, Model model){
        Page<CartItem> results = cartItemRepository.findAccountCartItemItemOptionFetchByAccountId(account.getId(), pageable);
        if(results.isEmpty()){
            Account findAccount = accountRepository.findById(account.getId()).get();
            model.addAttribute("account", findAccount);
            return "cart/main";
        }
        List<CartItem> cartItemList = results.getContent();
        // Item -> StoreOwner = 다대일 관계, 장바구니는 한번에 한 가게에서만 추가할수있음
        StoreOwner storeOwner = cartItemList.get(0).getItem().getStoreOwner();
        Cart findCart = cartItemList.get(0).getCart();  // CartItem -> Cart = 다대일 관계
        Account findAccount = findCart.getAccount();    // Cart -> Account = 일대일 관계이므로 모든 CartItem와 연관된 카트, 계정은 하나임

        Map<LocalDateTime, Item> itemMap = new HashMap<>();
        Map<LocalDateTime, List<ItemOption>> itemOptionMap = new HashMap<>();
        cartItemList.forEach(ci -> {
            itemMap.put(ci.getAddDate(), ci.getItem());
            itemOptionMap.put(ci.getAddDate(), ci.getItemOptionList());
        });

        model.addAttribute("account", findAccount);
        model.addAttribute(findCart);
        model.addAttribute(storeOwner);
        model.addAttribute("cartItemList", cartItemList);
        model.addAttribute("itemMap", itemMap);
        model.addAttribute("itemOptionMap", itemOptionMap);

        return "cart/main";
    }

    @PostMapping("/api/confirm")
    @ResponseBody
    public boolean cart_add_confirm(@CurrentUser Account account, @RequestBody CartAddDto cartAddDto){
        List<CartItem> findCartItemList = cartItemRepository.findAccountCartCartItemStoreFetchByAccountId(account.getId());
        Item newItem = itemRepository.findStoreFetchById(cartAddDto.getItemId());

        Cart cart;
        if (!findCartItemList.isEmpty()) {
            cart = findCartItemList.get(0).getCart(); // CartItem -> Cart 다대일 관계 -> 어떤 카트상품에서 카트를 추출해도 같은 카트이다.
            StoreOwner findStoreOwner = findCartItemList.get(0).getItem().getStoreOwner();

            // 기존 카트에 담긴 상품의 가게와 새로 추가한 상품의 가게가 일치하는지 확인. 일치하지않으면 기존 장바구니상품 모두 삭제
            StoreOwner addItemStoreOwner = newItem.getStoreOwner();
            if(!findStoreOwner.equals(addItemStoreOwner)){
                return true;
            }
        }
        return false;
    }

    @PostMapping("/api/add")
    @ResponseBody
    public ResponseEntity cart_add(@CurrentUser Account account, @RequestBody CartAddDto cartAddDto){
        if(account == null){
            return ResponseEntity.status(403).build();
        }
        ResponseEntity<Object> build = cartService.addItem(account, cartAddDto);
        if (build != null) return build;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{cartItemId}/delete")
    public String cartItem_delete(@CurrentUser Account account, @PathVariable Long cartItemId){
        cartService.deleteCartItem(account, cartItemId);
        return "redirect:/cart";
    }

}
