package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.CurrentUser;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.mystore.CategoryWrapper;
import com.devgyu.banchan.mystore.MystoreDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mystore")
public class StoreController {
    private final StoreOwnerRepository ownerRepository;
    private final StoreOwnerService ownerService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    @GetMapping({"", "/"})
    public String myStore_main(@CurrentUser StoreOwner storeOwner, @ModelAttribute MystoreDto mystoreDto,
                               Model model){
        StoreOwner findOwner = ownerRepository.findCategoriesFetchById(storeOwner.getId()).get(0);
        List<String> categories;
        if(findOwner.getStoreCategories() != null) {
            categories = findOwner.getStoreCategories()
                    .stream().map(sc -> sc.getCategory().getName()).collect(Collectors.toList());
        }else{
            categories = Collections.emptyList();
        }
        Address address = findOwner.getAddress();
        modelMapper.map(findOwner, mystoreDto);
        modelMapper.map(address, mystoreDto);
        mystoreDto.setCategories(categories);
        return "mystore/main";
    }
    @PostMapping("/modify")
    public String myStore_main_do(@CurrentUser StoreOwner storeOwner, @Valid @ModelAttribute MystoreDto mystoreDto,
                                  BindingResult result, Model model){
        // TODO 가게소개(StoreIntroduce)도 수정할 수 있도록 변경
        if(result.hasErrors()){
            return "mystore/main";
        }
        ownerService.modifyStoreOwner(storeOwner, mystoreDto);
        return "redirect:/mystore";
    }

    @GetMapping("/modify-password")
    public String modifyPassword(@CurrentUser StoreOwner storeOwner, @ModelAttribute ModifyPasswordDto modifyPasswordDto){

        return "mystore/change-password";
    }

    @PostMapping("/modify-password")
    public String modifyPassword_do(@CurrentUser StoreOwner storeOwner, @Valid @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                  BindingResult result, Model model){
        if(modifyPasswordDto.getPassword().equals("") || !passwordEncoder.matches(modifyPasswordDto.getPassword(), storeOwner.getPassword())){
            result.rejectValue("password", null, "인증에 실패 하였습니다.");
            return "mystore/change-password";
        }
        return "mystore/change-password-main";
    }
    @PostMapping("/modify-password-main")
    public String modifyPassword_main(@CurrentUser StoreOwner storeOwner, @ModelAttribute ModifyPasswordDto modifyPasswordDto,
                                      BindingResult result) throws IllegalAccessException {
        if(modifyPasswordDto.getPasswordRepeat().equals("")){
            result.rejectValue("passwordRepeat",null,"비밀번호 확인은 필수입니다.");
            return "mystore/change-password-main";
        }else if(modifyPasswordDto.getPassword().equals("")){
            result.rejectValue("password",null,"변경할 비밀번호를 입력해주세요.");
            return "mystore/change-password-main";
        }else if(!modifyPasswordDto.getPassword().equals(modifyPasswordDto.getPasswordRepeat())){
            result.rejectValue("passwordRepeat",null,"비밀번호가 일치하지 않습니다.");
            return "mystore/change-password-main";
        }
        ownerService.modifyPassword(storeOwner.getEmail(), modifyPasswordDto);
        return "redirect:/mystore";
    }
}
