package com.devgyu.banchan.storelist;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.ScrollingDto;
import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.CurrentUser;
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
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/storelist")
public class StoreListController {
    private final StoreListService storeListService;
    private final CategoryRepository categoryRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final AppProperties appProperties;
    @GetMapping({"/", ""})
    public String storeList(){
        return "storelist/main";
    }

    @GetMapping("/{category}")
    public String category(@CurrentUser Account account, @PathVariable String category, @PageableDefault Pageable pageable, Model model){

        String road = account.getAddress().getRoad();
        String gu = road.split(" ")[1];
        String convertedRoad = "";

        if (gu.indexOf('시') != -1) {
            convertedRoad = gu.split("시")[0] + "시";
        } else if (gu.indexOf('군') != -1) {
            convertedRoad = gu.split("군")[0] + "군";
        } else if (gu.indexOf('구') != -1) {
            convertedRoad = gu.split("구")[0] + "구";
        } else {
            throw new IllegalArgumentException("주문을 확인하던중 에러가 발생하였습니다. 관리자에게 문의 부탁드립니다.");
        }

        Page<StoreOwner> findStore = storeOwnerRepository.findCategoriesFetchByCategoryNameAndSigungu(category, convertedRoad,pageable);
        List<StoreListDto> collect = findStore.getContent()
                .stream().map(fs -> new StoreListDto(fs.getId(), fs.getName(), fs.getThumbnail(), fs.isManagerAuthenticated()))
                .collect(Collectors.toList());

        model.addAttribute("storeList", collect);
        model.addAttribute("category", category);
        return "storelist/list";
    }
    @GetMapping("/api/{category}")
    @ResponseBody
    public ScrollingDto category_scrolling(@CurrentUser Account account, @PathVariable String category, @PageableDefault Pageable pageable){

        String road = account.getAddress().getRoad();
        String gu = road.split(" ")[1];
        String convertedRoad = "";

        if (gu.indexOf('시') != -1) {
            convertedRoad = gu.split("시")[0] + "시";
        } else if (gu.indexOf('군') != -1) {
            convertedRoad = gu.split("군")[0] + "군";
        } else if (gu.indexOf('구') != -1) {
            convertedRoad = gu.split("구")[0] + "구";
        } else {
            throw new IllegalArgumentException("주문을 확인하던중 에러가 발생하였습니다. 관리자에게 문의 부탁드립니다.");
        }

        Page<StoreOwner> findStore = storeOwnerRepository.findCategoriesFetchByCategoryNameAndSigungu(category, convertedRoad,pageable);
        List<StoreListDto> collect = findStore.getContent()
                .stream().map(fs -> new StoreListDto(fs.getId(), fs.getName(), fs.getThumbnail(), fs.isManagerAuthenticated()))
                .collect(Collectors.toList());
        ScrollingDto scrollingDto = new ScrollingDto(collect, findStore.isLast());
        return scrollingDto;
    }
}
