package com.devgyu.banchan.storelist;

import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/storelist")
public class StoreListController {
    private final StoreListService storeListService;
    private final CategoryRepository categoryRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    @GetMapping({"/", ""})
    public String storeList(){
        return "storelist/main";
    }

    @GetMapping("/{category}")
    public String category(@PathVariable String category, @PageableDefault Pageable pageable, Model model){
        Page<StoreOwner> findStore = storeOwnerRepository.findCategoriesFetchByCategoryName(category, pageable);
        List<Category> all = categoryRepository.findAll();
        List<StoreListDto> collect = findStore.getContent()
                .stream().map(fs -> new StoreListDto(fs.getId(), fs.getName(), fs.getThumbnail()))
                .collect(Collectors.toList());
        model.addAttribute("storeList", collect);
        model.addAttribute("category", category);
        model.addAttribute("categoryList", all);
        return "storelist/list";
    }
}