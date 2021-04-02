package com.devgyu.banchan.items;

import com.devgyu.banchan.AppProperties;
import com.devgyu.banchan.account.Account;
import com.devgyu.banchan.modules.category.Category;
import com.devgyu.banchan.modules.category.CategoryRepository;
import com.devgyu.banchan.modules.storeowner.StoreOwner;
import com.devgyu.banchan.modules.storeowner.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final CategoryRepository categoryRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;

    public void addItem(StoreOwner storeOwner, String category, AddItemDto addItemDto) throws IOException {
        List<ItemOption> itemOptionList = addItemDto.getItemOptionList();

        Category findCategory = categoryRepository.findByName(category);
        StoreOwner findOwner = storeOwnerRepository.findById(storeOwner.getId()).get();
        Item item = new Item(addItemDto.getName(), addItemDto.getPrice(), addItemDto.getItemIntroduce(), findOwner, findCategory,
                addItemDto.getItemOptionList());
        saveImageFile(item, addItemDto.getThumbnailFile(), "item");
        findOwner.addItem(item);
        findCategory.addItem(item);
    }

    private void saveImageFile(Item item, MultipartFile thumbnailFile, String path) throws IOException {
        String uploadFolderPrefix = appProperties.getUploadFolderPrefix();
        File folder = new File(uploadFolderPrefix + "/" + path + "/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String extensionName = StringUtils.getFilenameExtension(thumbnailFile.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + LocalDateTime.now() + "." + extensionName;
        File fileProperty = new File(folder, fileName);
        thumbnailFile.transferTo(fileProperty);

        item.setThumbnail(fileName);
        item.setOriginThumbnail(thumbnailFile.getOriginalFilename());
    }

    public void modifyItem(AddItemDto addItemDto, Long itemId) throws IOException {
        Item findItem = itemRepository.findItemOptionCategoryFetchById(itemId);

        List<ItemOption> itemItemOptionList = findItem.getItemOptionList(); // 원래 상품에 있었던 상품 옵션
        List<ItemOptionDto> itemOptionDtoList = addItemDto.getItemOptionDtoList();
        itemOptionDtoList.removeIf(iod -> {
            if (iod.getName() == null || iod.getPrice() == 0) return true;
            return false;
        });

        if (addItemDto.getThumbnailFile().getSize() == 0) {
            String thumbnail = findItem.getThumbnail();
            findItem.setThumbnail(thumbnail);
        }else{
            saveImageFile(findItem, addItemDto.getThumbnailFile(), "item");
        }
        findItem.setName(addItemDto.getName());
        findItem.setPrice(addItemDto.getPrice());
        findItem.setItemIntroduce(addItemDto.getItemIntroduce());

        modifyItemOption(findItem, itemItemOptionList, itemOptionDtoList); // 상품 옵션 수정
    }

    private void modifyItemOption(Item findItem, List<ItemOption> itemItemOptionList, List<ItemOptionDto> itemOptionDtoList) {
        List<String> itemOptionDtoName = itemOptionDtoList.stream().map(iod -> iod.getName()).collect(Collectors.toList());
        List<ItemOption> findItemOptionList = itemOptionRepository
                .findAllByItemIdAndNamesIn(findItem.getId(), itemOptionDtoName);

        List<ItemOption> bindingList = new ArrayList<>();

        for (int a=0; a < itemOptionDtoList.size(); a++) {
            ItemOptionDto itemOptionDto = itemOptionDtoList.get(a);
            if(findItemOptionList.size() > a) {
                ItemOption itemOption = findItemOptionList.get(a);
                if(itemOption.getName().equals(itemOptionDto.getName())) {
                    bindingList.add(itemOption);
                }
            }else{
                ItemOption newItemOption = new ItemOption(itemOptionDto.getName(), itemOptionDto.getPrice(), findItem);
                bindingList.add(newItemOption);
            }
        }

        // itemItemOptionList 사이즈 = findOptionList
        // 이 작업으로 itemOptionDto로 들어온 값 중 기존 DB에 item과 연관된 itemOption값이 itemItemOptionList에 남게됨.
        // DB에 있는값들만 들어온건지, DB에 없는값이 새로 들어온건지 판단하기 위함.
        itemItemOptionList.removeIf(itemItemOption -> !findItemOptionList.contains(itemItemOption));

        if(bindingList.size() != itemItemOptionList.size()){ // DB에 없는 값이 들어온경우
            for (ItemOption bindingItemOption : bindingList) {
                if(!itemItemOptionList.contains(bindingItemOption)){
                    itemItemOptionList.add(bindingItemOption);
                }
            }
        }
    }
}
