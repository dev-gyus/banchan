package com.devgyu.banchan.storelist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/storelist")
public class StoreListController {
    @GetMapping({"/", ""})
    public String storeList(){
        return "storelist/main";
    }

    @GetMapping("/{category}")
    public String category(@PathVariable String category, Model model){

        return "storelist/list";
    }
}
