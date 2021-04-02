package com.devgyu.banchan;

import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.AccountService;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PrepareController {
    private final CategoryRepository categoryRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/prepare/addcategory")
    @Transactional
    public String addCategory() {
        List<String[]> categoryNames = Arrays.asList(new String[]{"김치", "kimchi"}, new String[]{"전", "jeon"},
                new String[]{"젓갈", "jeotgal"}, new String[]{"볶음", "bokum"}, new String[]{"무침", "muchim"}, new String[]{"찌개", "zzigae"},
                new String[]{"찜", "zzim"});
        List<Category> collect = categoryNames.stream().map(s -> new Category(s[0], s[1])).collect(Collectors.toList());
        categoryRepository.saveAll(collect);
        return "redirect:/";
    }

    @GetMapping("/prepare/addStoreOwner")
    @Transactional
    public String addStoreOwner() {
        String rowPassword = "vmffkd495";
        Address address = new Address("12345", "신림로", "오솔길11", "2층202호", null);
        List<Category> allCategories = categoryRepository.findAll();
        List<StoreOwner> newOwnerList = new ArrayList<>();
        for(int a=14; a < 34; a++) {
            StoreOwner newOwner = new StoreOwner("cjsworbehd" + a, "규스" + a, passwordEncoder.encode(rowPassword), "규스" + a, "01012341234", address, "021231234");
            for (Category category : allCategories) {
                StoreCategory storeCategory = new StoreCategory(newOwner, category);
            }
            newOwnerList.add(newOwner);
        }
        storeOwnerRepository.saveAll(newOwnerList);
        return "redirect:/";
    }
}