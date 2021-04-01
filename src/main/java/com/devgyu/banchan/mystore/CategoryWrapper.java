package com.devgyu.banchan.mystore;

import lombok.Data;

import java.util.List;

@Data
public class CategoryWrapper {
    List<String> categories;

    public CategoryWrapper(List<String> categories) {
        this.categories = categories;

    }
}
