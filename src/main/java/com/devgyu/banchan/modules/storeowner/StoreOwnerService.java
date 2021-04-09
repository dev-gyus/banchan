package com.devgyu.banchan.modules.storeowner;

import com.devgyu.banchan.account.Address;
import com.devgyu.banchan.account.dto.ModifyPasswordDto;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storecategory.StoreCategory;
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
    private final CategoryRepository categoryRepository;
    private final ReviewRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void modifyStoreOwner(StoreOwner storeOwner, MystoreDto mystoreDto) {
        StoreOwner findOwner = storeOwnerRepository.findCategoriesFetchById(storeOwner.getId()).get(0);
        // 실제 가게 정보에 대한 부분이 변경될 경우 관리자 승인을 받기 위해 관리자 승인여부 false로 둠
        if(findOwner.isStoreOwnerChanged(mystoreDto)){
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

    public void addStoreReview(StoreOwner storeOwner, Long reviewId, ReviewDto reviewDto) {
        Review customerReview = reviewRepository.findById(reviewId).get();
        if(customerReview == null) throw new IllegalArgumentException("잘못된 요청입니다");
        Review storeReview = new Review(reviewDto.getContent(), customerReview);
        reviewRepository.save(storeReview);
    }
}
