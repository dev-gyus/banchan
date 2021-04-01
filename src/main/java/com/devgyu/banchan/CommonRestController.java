package com.devgyu.banchan;

import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.mystore.CategoryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommonRestController {
    private final CategoryRepository categoryRepository;

    @GetMapping("/categories")
    public CategoryWrapper api_categories(){
        List<String> allCategories = categoryRepository.findAll().stream().map(c -> c.getName()).collect(Collectors.toList());

        return new CategoryWrapper(allCategories);
    }
}
