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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final ModelMapper modelMapper;
    private final AppProperties appProperties;

    public void addItem(StoreOwner storeOwner, String category, AddItemDto addItemDto) throws IOException {
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
        if(!folder.exists()){
            folder.mkdirs();
        }
        String extensionName = StringUtils.getFilenameExtension(thumbnailFile.getOriginalFilename());
        String fileName = UUID.randomUUID() + "_" + LocalDateTime.now() + "." + extensionName;
        File fileProperty = new File(folder, fileName);
        thumbnailFile.transferTo(fileProperty);

        item.setThumbnail(fileName);
        item.setOriginThumbnail(thumbnailFile.getOriginalFilename());
    }
}
