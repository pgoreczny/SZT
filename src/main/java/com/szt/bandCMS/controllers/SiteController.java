package com.szt.bandCMS.controllers;

import com.szt.bandCMS.models.Album;
import com.szt.bandCMS.models.Event;
import com.szt.bandCMS.models.NewsItem;
import com.szt.bandCMS.models.Subsite;
import com.szt.bandCMS.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/")
public class SiteController {

    final SubsitesService subsitesService;
    final AlbumService albumService;
    final ItemService itemService;
    final EventService eventService;
    final NewsService newsService;

    public SiteController(SubsitesService subsitesService, AlbumService albumService, ItemService itemService, EventService eventService, NewsService newsService) {
        this.subsitesService = subsitesService;
        this.albumService = albumService;
        this.itemService = itemService;
        this.eventService = eventService;
        this.newsService = newsService;
    }

    @GetMapping
    public String getMainPage(Model model) {
        List<String> albums = albumService.getAlbums()
                .stream()
                .map(album -> album.getTitle())
                .collect(Collectors.toList());
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("main", "common"));
        List<Event> events = eventService.getEventsVisible(2);
        List<NewsItem> news = newsService.getNews(3);
        Map<String, String> socialMedia = itemService.getItemsByPage("socialMedia");

        model.addAttribute("albums", albums);
        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("events", events);
        model.addAttribute("news", news);
        model.addAttribute("socialMedia", socialMedia);
        return "website/index/index";
    }

    @GetMapping(value = "/events")
    public String getEventsPage(Model model) {
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("events", "common"));
        List<Event> events = eventService.getEventsVisible();
        List<NewsItem> news = newsService.getNews(3);
        Map<String, String> socialMedia = itemService.getItemsByPage("socialMedia");

        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("events", events);
        model.addAttribute("news", news);
        model.addAttribute("socialMedia", socialMedia);


        return "website/events/index";
    }

    @GetMapping(value = "/albums")
    public String getAlbumsPage(Model model) {
        List<Album> albums = albumService.getAlbums();
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("albumMain", "common"));
        Map<String, String> socialMedia = itemService.getItemsByPage("socialMedia");

        model.addAttribute("albums", albums);
        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("socialMedia", socialMedia);
        return "website/album/index";
    }

    @GetMapping(value = "/albums/{album}")
    public String getAlbumPage(Model model, @PathVariable(value = "album") String album) {
        Optional<Album> albumData = albumService.getAlbumByName(album);
        List<Album> albums = albumService.getAlbums();
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("albumSpecific", "common"));
        Map<String, String> socialMedia = itemService.getItemsByPage("socialMedia");

        albumData = albums.stream().filter(a -> a.getCode().equals(album)).findFirst();
        if (albumData.isPresent()) {
            model.addAttribute("album", albumData.get());
            model.addAttribute("subsites", subsites);
            model.addAttribute("items", items);
            model.addAttribute("socialMedia", socialMedia);
            return "website/album/specific";
        } else {
            return "website/error/error";
        }
    }

    @GetMapping(value = "/news")
    public String getNewsPage(Model model) {
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("news", "common"));
        List<NewsItem> news = newsService.getNews();
        Map<String, String> socialMedia = itemService.getItemsByPage("socialMedia");

        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("news", news);
        model.addAttribute("socialMedia", socialMedia);
        return "website/news/index";
    }

    @GetMapping(value = "/subsite/{subpage}")
    public String getSimpleSubpage(Model model, @PathVariable(value = "subpage") String subpage) {
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("simple", "common"));
        Map<String, String> socialMedia = itemService.getItemsByPage("socialMedia");

        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("socialMedia", socialMedia);

        Optional<Subsite> site = subsites.stream().filter(o -> o.getName().toLowerCase().equals(subpage.toLowerCase())).findFirst();
        if (site.isPresent()) {
            model.addAttribute("content", site.get());
            return "website/subsite/index";
        }
        return "website/error/error";
    }
}
