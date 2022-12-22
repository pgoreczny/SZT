package com.szt.bandCMS.repositories;

import com.szt.bandCMS.models.NewsItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends CrudRepository<NewsItem, Long> {
    List<NewsItem> findAllByOrderByDateDesc();
}
