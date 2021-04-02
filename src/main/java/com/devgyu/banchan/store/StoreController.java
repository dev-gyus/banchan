package com.devgyu.banchan.store;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemRepository;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.mystore.MystoreDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {
    private final StoreOwnerRepository storeOwnerRepository;
    private final CategoryRepository categoryRepository;
    private final StoreService storeService;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;

    @GetMapping({"/{storeId}"})
    public String store_main(@PathVariable Long storeId, Model model){
        List<Category> categoryList = categoryRepository.findAllByStoreOwnerId(storeId);
        StoreOwner findOwner = categoryList.get(0).getStoreCategoryList().get(0).getStoreOwner();
        Address address = findOwner.getAddress();
        StoreDto map = modelMapper.map(findOwner, StoreDto.class);
        modelMapper.map(address, map);

        List<String> categoryNames = categoryList.stream().map(c -> c.getName()).collect(Collectors.toList());

        Map<String, List<Item>> itemMap = new HashMap<>();
        for (String categoryName : categoryNames) {
            List<Item> findItemList = itemRepository.findAllByCategoryName(categoryName);
            itemMap.put(categoryName, findItemList);
        }
        model.addAttribute("itemMap", itemMap);
        model.addAttribute(map);
        return "store/main";
    }
}
