package com.devgyu.banchan.items;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategoryRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.mystore.MystoreDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @GetMapping({"", "/"})
    public String item_main(@CurrentUser StoreOwner storeOwner, Model model){
        List<Category> categoryList = categoryRepository.findAllByStoreOwnerId(storeOwner.getId());
        StoreOwner findOwner = categoryList.get(0).getStoreCategoryList().get(0).getStoreOwner();
        Address address = findOwner.getAddress();
        MystoreDto map = modelMapper.map(findOwner, MystoreDto.class);
        modelMapper.map(address, map);

        List<String> categoryNames = categoryList.stream().map(c -> c.getName()).collect(Collectors.toList());

        Map<String, List<Item>> itemMap = new HashMap<>();
        for (String categoryName : categoryNames) {
            List<Item> findItemList = itemRepository.findAllByCategoryName(categoryName);
            itemMap.put(categoryName, findItemList);
        }
        model.addAttribute("itemMap", itemMap);
        model.addAttribute(map);
        return "item/main";
    }

    @GetMapping("/add")
    public String add_item(@RequestParam String category, @ModelAttribute AddItemDto addItemDto, Model model){
        model.addAttribute("category", category);
        return "item/add-item";
    }
    @PostMapping("/add")
    public String add_item_do(@CurrentUser StoreOwner storeOwner, @RequestParam String category, @Valid @ModelAttribute AddItemDto addItemDto,
                              BindingResult result, Model model) throws IOException {
        if(result.hasErrors()){
            model.addAttribute("category",category);
            return "item/add-item";
        }
        List<ItemOption> itemOptionList = addItemDto.getItemOptionList();
        itemOptionList.removeIf(iol -> iol.getName() == null);
        addItemDto.setItemOptionList(itemOptionList);
        itemService.addItem(storeOwner, category, addItemDto);
        return "redirect:/items";
    }
}
