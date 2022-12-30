package com.szt.bandCMS.controllers;

import com.szt.bandCMS.models.Subsite;
import com.szt.bandCMS.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    public String getMainPage(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("main", "common"));
        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        model.addAttribute("items", itemService.getItemsByPage(List.of("main", "common")));
        model.addAttribute("colors", List.of("Black", "Grey", "White", "Red", "Orange", "Yellow", "Green", "Blue"));
        return "admin/main";
    }

    @PostMapping("/main")
    public RedirectView saveMainPage(RedirectAttributes attributes, @RequestParam("head_text") String headText, @RequestParam("color") String color) {
        itemService.saveItem("head_text", headText);
        itemService.saveItem("color", color);
        attributes.addFlashAttribute("success", "Page saved successfully");
        return new RedirectView("/admin/main");
    }

    @PostMapping("/uploadLogo")
    public RedirectView uploadImage(RedirectAttributes attributes, @RequestParam("file") MultipartFile file) {
        try {
            String path = fileStorageService.save(file, "logo");
            itemService.saveItem("logo", path);
        }
        catch (IllegalArgumentException exception) {
            attributes.addFlashAttribute("error", "The file has wrong size or extension. Try using another.");
            return new RedirectView("/admin/main");
        } catch (IOException e) {
            attributes.addFlashAttribute("error", "An error occured while saving this file. Please try again.");
            return new RedirectView("/admin/main");
        }
        attributes.addFlashAttribute("success", "Image saved successfully");
        return new RedirectView("/admin/main");
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
