package com.devgyu.banchan.modules.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public void addCategory(String name, String urlName){
        Category category = new Category(name, urlName);
        categoryRepository.save(category);
    }
}
