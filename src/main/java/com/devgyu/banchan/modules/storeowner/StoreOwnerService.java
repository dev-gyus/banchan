package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.mystore.MystoreDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreOwnerService {
    private final StoreOwnerRepository storeOwnerRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public void modifyStoreOwner(StoreOwner storeOwner, MystoreDto mystoreDto) {
        StoreOwner findOwner = storeOwnerRepository.findCategoriesFetchById(storeOwner.getId()).get(0);
            Address modifyAddress =
                    new Address(mystoreDto.getZipcode(), mystoreDto.getRoad(), mystoreDto.getJibun(), mystoreDto.getDetail(), mystoreDto.getExtra());
        findOwner.setAddress(modifyAddress);
        findOwner.setNickname(mystoreDto.getName());
        findOwner.setName(mystoreDto.getName());
        findOwner.setPhone(mystoreDto.getPhone());
        findOwner.setTel(mystoreDto.getTel());
            if(!mystoreDto.getThumbnail().equals("")) {
                findOwner.setThumbnail(mystoreDto.getThumbnail());
            }
        List<String> categories = mystoreDto.getCategories();
        List<Category> ownerCategory = findOwner.getStoreCategories().stream().map(sc -> sc.getCategory()).collect(Collectors.toList());
        if(categories.size() > ownerCategory.size()) {
            List<Category> mystoreCategory = categoryRepository.findAllByNameIn(categories);
            mystoreCategory.forEach(mc -> {
                if (!ownerCategory.contains(mc)) {
                    new StoreCategory(findOwner, mc);
                }
            });
        }else if(categories.size() < ownerCategory.size()){
            List<Category> mystoreCategory = categoryRepository.findAllByNameIn(categories);
            List<StoreCategory> ownerStoreCategories = findOwner.getStoreCategories();
            List<StoreCategory> removeTargets = new ArrayList<>();
            ownerStoreCategories.forEach(osc -> {
                if (!mystoreCategory.contains(osc.getCategory())) {
                    removeTargets.add(osc);
                }
            });
            findOwner.getStoreCategories().removeAll(removeTargets);
        }
    }
    public void modifyPassword(String email, ModifyPasswordDto modifyPasswordDto) throws IllegalAccessException {
        StoreOwner findOwner = storeOwnerRepository.findByEmail(email);
        if(findOwner == null){
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }
        findOwner.setPassword(passwordEncoder.encode(modifyPasswordDto.getPassword()));
    }
}