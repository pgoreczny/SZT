package com.szt.bandCMS.services;

import com.szt.bandCMS.models.Item;
import com.szt.bandCMS.repositories.ItemRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Map<String, String> getItemsByPage(String page) {
        return itemRepository.findByPage(page)
                .stream()
                .collect(Collectors.toMap(Item::getKey, Item::getText));
    }

    public Map<String, String> getItemsByPage(List<String> pages) {
        return pages.stream()
                .map(page -> itemRepository.findByPage(page))
                .flatMap(List::stream)
                .collect(Collectors.toMap(Item::getKey, Item::getText));
    }

    @Transactional
    public void saveItem(String key, String value) {
        Item toChange = itemRepository.findByKey(key);
        toChange.setText(value);
        itemRepository.save(toChange);
    }
}
