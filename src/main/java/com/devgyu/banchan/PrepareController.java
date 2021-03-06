package com.devgyu.banchan;

import com.devgyu.banchan.account.*;
import com.devgyu.banchan.cart.Cart;
import com.devgyu.banchan.cart.CartRepository;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemOption;
import com.devgyu.banchan.items.ItemRepository;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.counselor.CounselorService;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderRepository;
import com.devgyu.banchan.modules.rider.query.RiderRepositoryImpl;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwnerService;
import com.devgyu.banchan.orders.Orders;
import com.devgyu.banchan.ordersitem.OrdersItem;
import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PrepareController {
    private final CategoryRepository categoryRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RiderRepository riderRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final CounselorService counselorService;
    private final AccountRepository accountRepository;
    private final ItemRepository itemRepository;
    private final ReviewRepository reviewRepository;
    private final EntityManager em;

    @GetMapping("/prepare/add-category")
    @Transactional
    public String addCategory() {
        List<String[]> categoryNames = Arrays.asList(new String[]{"??????", "kimchi"}, new String[]{"???", "jeon"},
                new String[]{"??????", "jeotgal"}, new String[]{"??????", "bokum"}, new String[]{"??????", "muchim"}, new String[]{"??????", "zzigae"},
                new String[]{"???", "zzim"});
        List<Category> collect = categoryNames.stream().map(s -> new Category(s[0], s[1])).collect(Collectors.toList());
        categoryRepository.saveAll(collect);
        return "redirect:/";
    }

    @GetMapping("/prepare/add-store-owner")
    @Transactional
    public String addStoreOwner() {
        String rowPassword = "vmffkd495";
        Address address = new Address("12345", "????????? 11???", "?????????11", "2???202???", null);
        List<Category> allCategories = categoryRepository.findAll();
        List<StoreOwner> newOwnerList = new ArrayList<>();
        for (int a = 14; a < 34; a++) {
            StoreOwner newOwner = new StoreOwner("cjsworbehd" + a, "??????" + a, passwordEncoder.encode(rowPassword), "??????" + a, "01012341234", address, "021231234");
            for (Category category : allCategories) {
                StoreCategory storeCategory = new StoreCategory(newOwner, category);
            }
            newOwnerList.add(newOwner);
        }
        storeOwnerRepository.saveAll(newOwnerList);
        return "redirect:/";
    }

    @GetMapping("/prepare/add-rider")
    @Transactional
    public String addRider() {
        String rowPassword = "vmffkd495";
        Address address = new Address("12345", "????????? ????????? 11???", "?????????11", "2???202???", null);
        List<Rider> newRider = new ArrayList<>();
        for (int a = 100; a < 134; a++) {
            Rider rider = new Rider("cjsworbehd" + a, "??????" + a, passwordEncoder.encode(rowPassword), "??????" + a, "01012341234", address, "021231234", "12-12-123456-12");
            newRider.add(rider);
        }
        riderRepository.saveAll(newRider);
        return "redirect:/";
    }

    @GetMapping("/prepare/add-store-item")
    @Transactional
    public String addStoreItem() {
        List<StoreOwner> storeOwners = storeOwnerRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        for (StoreOwner storeOwner : storeOwners) {
            if (storeOwner.getId() != 26) {
                for (Category category : categories) {
                    List<ItemOption> itemOptions = new ArrayList<>();
                    Item newItem = new Item("?????????1", 10000, "????????????????????????.", storeOwner, category, itemOptions);
                    for (int a = 0; a < 2; a++) {
                        ItemOption newItemOption = new ItemOption("?????????1????????????" + a, 1000, newItem);
                    }
                }
            }
        }
        return "redirect:/";
    }

    @GetMapping("/prepare/addcart")
    @Transactional
    public String addCart(@CurrentUser Account account) {
        if (account != null) {
            List<Cart> carts = cartRepository.existByAccountId(account.getId());
            if (carts.isEmpty()) {
                Cart cart = new Cart(account);
                cartRepository.save(cart);
            }
        }
        return "redirect:/";
    }

    @GetMapping("/prepare/counselor")
    public String prepare_counselor(){
        counselorService.saveCounselor();
        return"redirect:/";
    }

}
