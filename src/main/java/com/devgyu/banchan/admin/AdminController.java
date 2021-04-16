package com.devgyu.banchan.admin;

import com.devgyu.banchan.CommonUtils;
import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.account.AccountRepository;
import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.admin.dto.*;
import com.devgyu.banchan.items.Item;
import com.devgyu.banchan.items.ItemRepository;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.rider.Rider;
import com.devgyu.banchan.modules.rider.RiderRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import com.devgyu.banchan.store.StoreDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final AdminRepository adminRepository;
    private final AccountRepository accountRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final RiderRepository riderRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ItemRepository itemRepository;

    @GetMapping({"", "/"})
    public String admin_main(Model model) {
        return "admin/main";
    }

    @GetMapping("/user-block")
    public String user_block(Model model) {
        return "admin/user-block";
    }

    @PostMapping("/user-block/search")
    public String user_block(@RequestParam String firstCondition, @RequestParam String secondCondition, Model model) {
        List<Account> accountList = adminRepository.findAccountByConditions(firstCondition, secondCondition);
        if (accountList.isEmpty()) {
            model.addAttribute("firstCondition", firstCondition);
            model.addAttribute("secondCondition", secondCondition);
            return "admin/user-block";
        }
        Account account = accountList.get(0);
        commonBlockMethod(firstCondition, secondCondition, model, account);
        return "admin/user-block";
    }

    @PostMapping("/user-block/{accountId}")
    public String user_block_do(@PathVariable Long accountId, @RequestParam String firstCondition, @RequestParam String secondCondition,
                                @RequestParam String blockReason, Model model) {
        Account targetAccount = adminService.userBlock(accountId, blockReason);
        commonBlockMethod(firstCondition, secondCondition, model, targetAccount);
        return "admin/user-block";
    }

    @PostMapping("/user-unblock/{accountId}")
    public String user_unBlock_do(@PathVariable Long accountId, @RequestParam String firstCondition, @RequestParam String secondCondition,
                                  Model model) {
        Account targetAccount = adminService.userUnBlock(accountId);
        commonBlockMethod(firstCondition, secondCondition, model, targetAccount);
        return "admin/user-block";
    }

    private void commonBlockMethod(String firstCondition, String secondCondition, Model model, Account account) {
        AdminBlockDto adminBlockDto = new AdminBlockDto(account.getId(), account.getBlockReason(),
                account.getNickname(), account.getBlockedDate(), account.getUnblockedDate(), account.getBlockCount(), account.isBlocked());

        model.addAttribute("account", adminBlockDto);
        model.addAttribute("firstCondition", firstCondition);
        model.addAttribute("secondCondition", secondCondition);
    }

    @GetMapping("/store-accept")
    public String admin_store_accept(@ModelAttribute AdminStoreDto adminStoreDto) {
        return "admin/store-accept";
    }

    @GetMapping("/rider-accept")
    public String admin_rider_accept(@ModelAttribute AdminRiderDto adminRiderDto) {
        return "admin/rider-accept";
    }

    @PostMapping("/store-accept")
    public String admin_user_accept_do(@ModelAttribute AdminStoreDto adminStoreDto, @PageableDefault Pageable pageable, Model model) {
        String road = adminStoreDto.getRoad();
        if (road == null) {
            return "admin/store-accept";
        }
        AdminStoreApiDto adminStoreApiDto = getAdminStoreApiDtoList(pageable, road);

        model.addAttribute("road", road);
        model.addAttribute("storeList", adminStoreApiDto.getAdminStoreDtoList());

        return "admin/store-accept";
    }

    @PostMapping("/rider-accept")
    public String admin_rider_accept_do(@ModelAttribute AdminRiderDto adminRiderDto, @PageableDefault Pageable pageable, Model model) {
        String road = adminRiderDto.getRoad();
        if (road == null) {
            return "admin/rider-accept";
        }
        AdminRiderApiDto adminRiderApiDto = getAdminRiderApiDtoList(pageable, road);

        model.addAttribute("road", road);
        model.addAttribute("riderList", adminRiderApiDto.getAdminRiderDtoList());

        return "admin/rider-accept";
    }

    @PostMapping("/store-accept/{storeId}")
    public String store_accept_do(@PathVariable Long storeId, @ModelAttribute AdminStoreDto adminStoreDto, @PageableDefault Pageable pageable,
                                  @RequestParam String road, Model model) {
        adminService.storeAccept(storeId);
        if (road == null) {
            return "admin/store-accept";
        }
        AdminStoreApiDto adminStoreApiDto = getAdminStoreApiDtoList(pageable, road);

        model.addAttribute("road", road);
        model.addAttribute("storeList", adminStoreApiDto.getAdminStoreDtoList());
        return "admin/store-accept";
    }

    @PostMapping("/rider-accept/{riderId}")
    public String rider_accept_do(@PathVariable Long riderId, @ModelAttribute AdminRiderDto adminRiderDto, @PageableDefault Pageable pageable,
                                  @RequestParam String road, Model model) {
        adminService.riderAccept(riderId);
        if (road == null) {
            return "admin/rider-accept";
        }
        AdminRiderApiDto adminRiderApiDto = getAdminRiderApiDtoList(pageable, road);

        model.addAttribute("road", road);
        model.addAttribute("riderList", adminRiderApiDto.getAdminRiderDtoList());
        return "admin/rider-accept";
    }

    @PostMapping("/store-reject/{storeId}")
    public String store_reject_do(@PathVariable Long storeId, @ModelAttribute AdminStoreDto adminStoreDto, @RequestParam String rejectReason,
                                  @PageableDefault Pageable pageable, @RequestParam String road, Model model) {
        adminService.storeReject(storeId, rejectReason);
        if (road == null) {
            return "admin/store-accept";
        }
        AdminStoreApiDto adminStoreApiDto = getAdminStoreApiDtoList(pageable, road);

        model.addAttribute("road", road);
        model.addAttribute("storeList", adminStoreApiDto.getAdminStoreDtoList());
        return "admin/store-accept";
    }

    @PostMapping("/rider-reject/{riderId}")
    public String rider_reject_do(@PathVariable Long riderId, @ModelAttribute AdminRiderDto adminRiderDto, @RequestParam String rejectReason,
                                  @PageableDefault Pageable pageable, @RequestParam String road, Model model) {
        adminService.riderReject(riderId, rejectReason);
        if (road == null) {
            return "admin/rider-accept";
        }
        AdminRiderApiDto adminRiderApiDto = getAdminRiderApiDtoList(pageable, road);

        model.addAttribute("road", road);
        model.addAttribute("riderList", adminRiderApiDto.getAdminRiderDtoList());
        return "admin/rider-accept";
    }

    @GetMapping("/api/{road}/store-accept")
    @ResponseBody
    public AdminStoreApiDto api_admin_store_accept(@PathVariable String road, @PageableDefault Pageable pageable) {

        AdminStoreApiDto adminStoreApiDto = getAdminStoreApiDtoList(pageable, road);

        return adminStoreApiDto;
    }

    @GetMapping("/api/{road}/rider-accept")
    @ResponseBody
    public AdminRiderApiDto api_admin_rider_accept(@PathVariable String road, @PageableDefault Pageable pageable) {

        AdminRiderApiDto adminRiderApiDto = getAdminRiderApiDtoList(pageable, road);

        return adminRiderApiDto;
    }

    private AdminStoreApiDto getAdminStoreApiDtoList(@PageableDefault Pageable pageable, String road) {
        String sigungu = CommonUtils.splitSigungu(road);

        Page<StoreOwner> findOwner = storeOwnerRepository.findAllByManagerAuthenticatedAndSigungu(false, sigungu, pageable);

        List<AdminStoreDto> collect = findOwner.getContent()
                .stream().map(so -> new AdminStoreDto(so.getId(), so.getNickname(), so.getAddress().getRoad(), so.getTel())).collect(Collectors.toList());
        return new AdminStoreApiDto(collect, findOwner.isLast());
    }

    private AdminRiderApiDto getAdminRiderApiDtoList(@PageableDefault Pageable pageable, String road) {
        String sigungu = CommonUtils.splitSigungu(road);

        Page<Rider> findRider = riderRepository.findAllByManagerAuthenticatedAndSigungu(false, sigungu, pageable);

        List<AdminRiderDto> collect = findRider.getContent()
                .stream().map(r -> new AdminRiderDto(r.getId(), r.getNickname(), r.getName(), r.getAddress().getRoad(), r.getDriverLicense())).collect(Collectors.toList());
        return new AdminRiderApiDto(collect, findRider.isLast());
    }

    @GetMapping({"/store/{storeId}"})
    public String store_main(@CurrentUser Admin admin, @PathVariable Long storeId, Model model) {
        List<Category> categoryList = categoryRepository.findAllByStoreOwnerId(storeId);
        StoreOwner findOwner = categoryList.get(0).getStoreCategoryList().get(0).getStoreOwner();
        Address ownerAddress = findOwner.getAddress();

        StoreDto map = modelMapper.map(findOwner, StoreDto.class);
        modelMapper.map(ownerAddress, map);

        List<String> categoryNames = categoryList.stream().map(c -> c.getName()).collect(Collectors.toList());

        Map<String, List<Item>> itemMap = new HashMap<>();
        for (String categoryName : categoryNames) {
            List<Item> findItemList = itemRepository.findAllByCategoryAndStore(categoryName, storeId);
            itemMap.put(categoryName, findItemList);
        }
        model.addAttribute("itemMap", itemMap);
        model.addAttribute(map);
        return "admin/store-main";
    }
}
