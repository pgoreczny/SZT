package com.szt.bandCMS.repositories;

import com.szt.bandCMS.models.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
    List<Item>findByPage(String page);
    Item findByKey(String key);
}
