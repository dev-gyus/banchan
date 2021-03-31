package com.devgyu.banchan.modules.category;

import lombok.RequiredArgsConstructor;
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
}
