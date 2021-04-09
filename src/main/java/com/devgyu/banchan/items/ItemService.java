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
        findOwner.setManagerAuthenticated(false);
        Item item = new Item(addItemDto.getName(), addItemDto.getPrice(), addItemDto.getItemIntroduce(), findOwner, findCategory,
                addItemDto.getItemOptionList());
        saveImageFile(item, addItemDto.getThumbnailFile(), "item");
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
        StoreOwner itemStoreOwner = findItem.getStoreOwner();

        if(findItem.isItemChanged(addItemDto)){
            itemStoreOwner.setManagerAuthenticated(false);
        }

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

        StoreOwner storeOwner = findItem.getStoreOwner(); // Item Id로 조회해왔기 때문에 결과 하나 밖에 없음

        if(!itemItemOptionList.isEmpty()) {
            List<ItemOption> bindingList = new ArrayList<>();

            for (int a = 0; a < itemOptionDtoList.size(); a++) {
                ItemOptionDto itemOptionDto = itemOptionDtoList.get(a);
                if (itemItemOptionList.size() > a) {
                    ItemOption itemOption = itemItemOptionList.get(a);
                    if (itemOption.getName().equals(itemOptionDto.getName()) && itemOption.getPrice() == itemOptionDto.getPrice()) {
                        bindingList.add(itemOption);
                    } else {
                        bindingList.add(new ItemOption(itemOptionDto.getName(), itemOptionDto.getPrice(), findItem));
                    }
                } else {
                    // 상품 옵션을 추가하는것은 결국 옵션을 변경하는 것이기때문에 상태 변경함
                    ItemOption newItemOption = new ItemOption(itemOptionDto.getName(), itemOptionDto.getPrice(), findItem);
                    bindingList.add(newItemOption);
                    storeOwner.setManagerAuthenticated(false);
                }
            }
            // itemItemOptionList의 값 = bindingList에서 itemItemOptionList의 값과 비교해서 없는값 지움
            // 이 작업으로 기존에 존재하던 값 중 새로 들어온 값에 없는 값은 삭제되고 아래 for에서 bindingList값과 동일하게 세팅됨
            List<ItemOption> removeItemOptionList = new ArrayList<>();
            itemItemOptionList.forEach(itemItemOption -> {
                if(!bindingList.contains(itemItemOption)){
                    removeItemOptionList.add(itemItemOption);
                    storeOwner.setManagerAuthenticated(false);
                }
            });
            itemItemOptionList.removeAll(removeItemOptionList);

            for (ItemOption bindingItemOption : bindingList) {
                if (!itemItemOptionList.contains(bindingItemOption)) {
                    itemItemOptionList.add(bindingItemOption);
                    storeOwner.setManagerAuthenticated(false); // 상품 옵션 변경시 관리자 승인 받아야함
                }
            }
        }else{  // 기존 상품옵션이 비어있는데 dto에 옵션이 있는경우는 반드시 추가임
            if(!itemOptionDtoList.isEmpty()) {
                for (int a = 0; a < itemOptionDtoList.size(); a++) {
                    ItemOptionDto itemOptionDto = itemOptionDtoList.get(a);
                    ItemOption itemOption = new ItemOption(itemOptionDto.getName(), itemOptionDto.getPrice(), findItem);
                }
                storeOwner.setManagerAuthenticated(false); // 상품 옵션 변경시 관리자 승인 받아야함
            }
        }
    }
}
