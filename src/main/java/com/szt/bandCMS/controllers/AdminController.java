package com.szt.bandCMS.controllers;

import com.szt.bandCMS.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    final SubsitesService subsitesService;
    final AlbumService albumService;
    final ItemService itemService;
    final EventService eventService;
    final NewsService newsService;
    final FileStorageService fileStorageService;

    public AdminController(SubsitesService subsitesService, AlbumService albumService, ItemService itemService, EventService eventService, NewsService newsService, FileStorageService fileStorageService) {
        this.subsitesService = subsitesService;
        this.albumService = albumService;
        this.itemService = itemService;
        this.eventService = eventService;
        this.newsService = newsService;
        this.fileStorageService = fileStorageService;
    }
    @GetMapping
    public String getAdminPage(Model model) {
        return "admin/index";
    }

    @GetMapping("/main")
    public String getMainPage(Model model) {
        return "admin/main";
    }

    @GetMapping("/uploadLogo")
    public String uploadImagePage(Model model) {
        return "admin/main";
    }

    @PostMapping("/uploadLogo")
    public String uploadImage(Model model, @RequestParam("file") MultipartFile file) {
        try {
            String path = fileStorageService.save(file, "logo");
            itemService.saveItem("logo", path);
        }
        catch (IllegalArgumentException exception) {
            model.addAttribute("error", "The file has wrong size or extension. Try using another.");
            return "admin/main";
        } catch (IOException e) {
            model.addAttribute("error", "An error occured while saving this file. Please try again.");
        }
        model.addAttribute("success", "Image saved successfully");
        return "admin/main";
    }

    @GetMapping("/events")
    public String getEvents(Model model) {
        return "admin/events";
    }

    @GetMapping("/news")
    public String getNews(Model model) {
        return "admin/news";
    }

    @GetMapping("/albums")
    public String getAlbums(Model model) {
        return "admin/album/index";
    }

    @GetMapping("/albums/{album}")
    public String getSpecificAlbum(Model model, @PathVariable(value = "album") String album) {
        return "admin/album/album";
    }

    @GetMapping("/subpages")
    public String getSubpages(Model model) {
        return "admin/subpage/index";
    }

    @GetMapping("/subpages/{subpage}")
    public String getSpecificSubpage(Model model, @PathVariable(value = "subpage") String subpage) {
        return "admin/subpage/subpage";
    }

    @GetMapping("/social")
    public String getSocialMedia(Model model) {
        return "admin/social";
    }
    @GetMapping("/settings")
    public String getSettings(Model model) {
        return "admin/settings";
    }
}
