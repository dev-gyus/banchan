package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
import com.devgyu.banchan.modules.storecategory.StoreCategoryRepository;
import com.devgyu.banchan.mystore.MystoreDto;
import com.devgyu.banchan.review.Review;
import com.devgyu.banchan.review.ReviewDto;
import com.devgyu.banchan.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final StoreCategoryRepository storeCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void modifyStoreOwner(StoreOwner storeOwner, MystoreDto mystoreDto) {
        StoreOwner findOwner = storeOwnerRepository.findCategoriesFetchById(storeOwner.getId()).get(0);
        // 실제 가게 정보에 대한 부분이 변경될 경우 관리자 승인을 받기 위해 관리자 승인여부 false로 둠
        if (findOwner.isStoreOwnerChanged(mystoreDto)) {
            findOwner.setManagerAuthenticated(false);
        }
        Address modifyAddress =
                new Address(mystoreDto.getZipcode(), mystoreDto.getRoad(), mystoreDto.getJibun(), mystoreDto.getDetail(), mystoreDto.getExtra());
        findOwner.setAddress(modifyAddress);
        findOwner.setNickname(mystoreDto.getName());
        findOwner.setName(mystoreDto.getName());
        findOwner.setPhone(mystoreDto.getPhone());
        findOwner.setTel(mystoreDto.getTel());
        findOwner.setStoreIntroduce(mystoreDto.getStoreIntroduce());
        if (!mystoreDto.getThumbnail().equals("")) {
            findOwner.setThumbnail(mystoreDto.getThumbnail());
        }

        List<String> categories = mystoreDto.getCategories();
        if (!categories.isEmpty()) {
            modifyCategory(findOwner, categories);
        }else{
            findOwner.getStoreCategories().removeIf(sc -> true);
        }

    }

    private void modifyCategory(StoreOwner findOwner, List<String> categories) {
        List<Category> ownerCategory = findOwner.getStoreCategories().stream().map(sc -> sc.getCategory()).collect(Collectors.toList());
        List<Category> modifyCategory = categoryRepository.findAllByNameIn(categories);
        // 원래 가게에 카테고리가 있었으나 수정할때 없어진 카테고리 삭제
        List<Category> removeCategoryList = new ArrayList<>();
        List<StoreCategory> removeStoreCategoryList = new ArrayList<>();
        for (Category category : ownerCategory) {
            if (!modifyCategory.contains(category)) {
                removeCategoryList.add(category);
            }
        }

        findOwner.getStoreCategories().forEach(sc -> {
            if (removeCategoryList.contains(sc.getCategory())) removeStoreCategoryList.add(sc);
        });

        findOwner.getStoreCategories().removeAll(removeStoreCategoryList);

        // 원래 가게에 없던 카테고리를 수정할때 추가한 경우 카테고리 추가
        List<StoreCategory> addTargetList = new ArrayList<>();
        for (Category category : modifyCategory) {
            if (!ownerCategory.contains(category)) {
                StoreCategory newStoreCategory = new StoreCategory(findOwner, category);
                addTargetList.add(newStoreCategory);
            }
        }
        storeCategoryRepository.saveAll(addTargetList);
    }

    public void modifyPassword(String email, ModifyPasswordDto modifyPasswordDto) throws IllegalAccessException {
        StoreOwner findOwner = storeOwnerRepository.findByEmail(email);
        if (findOwner == null) {
            throw new IllegalAccessException("잘못된 접근 입니다.");
        }
        findOwner.setPassword(passwordEncoder.encode(modifyPasswordDto.getPassword()));
    }

    public void addStoreReview(StoreOwner storeOwner, Long reviewId, ReviewDto reviewDto) {
        Review customerReview = reviewRepository.findById(reviewId).get();
        if (customerReview == null) throw new IllegalArgumentException("잘못된 요청입니다");
        Review storeReview = new Review(reviewDto.getContent(), customerReview);
        reviewRepository.save(storeReview);
    }
}
