package com.szt.bandCMS.services;

import com.szt.bandCMS.models.NewsItem;
import com.szt.bandCMS.repositories.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsItem> getNews() {
        return newsRepository.findAllByOrderByDateDesc();
    }

    public List<NewsItem> getNews(int count) {
        return newsRepository.findAllByOrderByDateDesc()
                .stream()
                .limit(count)
                .map(newsItem -> new NewsItem(newsItem.getDate(), newsItem.getTitle(), newsItem.getContent().substring(0, Math.min(newsItem.getContent().length(), 100))))
                .collect(Collectors.toList());
    }

    public Optional<NewsItem> getNewsById(long id) {
        return newsRepository.findById(id);
    }

    public void deleteNews(long id) {
        newsRepository.deleteById(id);
    }

    public void save(NewsItem newsItem) {
        newsRepository.save(newsItem);
    }
}
