package com.devgyu.banchan.mystore;

import com.devgyu.banchan.account.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mystore")
public class StoreController {
    private final StoreOwnerRepository ownerRepository;
    private final StoreOwnerService ownerService;
    @GetMapping({"", "/"})
    public String myStore_main(@CurrentUser StoreOwner storeOwner, @ModelAttribute MystoreMainDto mystoreMainDto,
                               Model model){
        return "mystore/main";
    }
    @PostMapping("")
    public String myStore_main_do(@CurrentUser StoreOwner storeOwner, @Valid @ModelAttribute MystoreMainDto mystoreMainDto,
                                  BindingResult result, Model model){
        return "mystore/main";
    }
}
