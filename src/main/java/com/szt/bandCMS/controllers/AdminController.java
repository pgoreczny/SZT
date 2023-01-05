package com.szt.bandCMS.controllers;

import com.szt.bandCMS.models.Album;
import com.szt.bandCMS.models.Event;
import com.szt.bandCMS.models.NewsItem;
import com.szt.bandCMS.models.Subsite;
import com.szt.bandCMS.services.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    final SubsitesService subsitesService;
    final AlbumService albumService;
    final ItemService itemService;
    final EventService eventService;
    final NewsService newsService;
    final FileStorageService fileStorageService;
    final UserService userService;

    public AdminController(SubsitesService subsitesService, AlbumService albumService, ItemService itemService, EventService eventService, NewsService newsService, FileStorageService fileStorageService, UserService userService) {
        this.subsitesService = subsitesService;
        this.albumService = albumService;
        this.itemService = itemService;
        this.eventService = eventService;
        this.newsService = newsService;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    @GetMapping
    public String getAdminPage(Model model) {
        return "admin/index";
    }

    //region <Main>
    @GetMapping("/main")
    public String getMainPage(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        List<Subsite> subsites = subsitesService.getHeadSubsites();
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("main", "common"));
        model.addAttribute("subsites", subsites);
        model.addAttribute("items", items);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        model.addAttribute("items", items);
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
        } catch (IllegalArgumentException exception) {
            attributes.addFlashAttribute("error", "The file has wrong size or extension. Try using another.");
            return new RedirectView("/admin/main");
        } catch (IOException e) {
            attributes.addFlashAttribute("error", "An error occured while saving this file. Please try again.");
            return new RedirectView("/admin/main");
        }
        attributes.addFlashAttribute("success", "Image saved successfully");
        return new RedirectView("/admin/main");
    }

    //endregion
    //region <Events>
    @GetMapping("/events")
    public String getEvents(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        List<Event> events = eventService.getEvents();
        model.addAttribute("success", success);
        model.addAttribute("editPath", "/admin/events/{id}");
        model.addAttribute("deletePath", "/admin/events/delete/{id}");
        model.addAttribute("error", error);
        model.addAttribute("events", events);
        return "admin/events/index";
    }

    @GetMapping("/events/{event}")
    public String getEvent(Model model, @PathVariable(value = "event") long id, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        Optional<Event> event = eventService.getEventById(id);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        if (event.isPresent()) {
            model.addAttribute("event", event.get());
            model.addAttribute("statuses", eventService.getAvailableStatuses(event.get().getStatus()));
        } else {
            if (id == 0) {
                Event newEvent = new Event();
                newEvent.setStatus("Hidden");
                model.addAttribute("event", newEvent);
                model.addAttribute("statuses", eventService.getAvailableStatuses(""));
            } else {
                model.addAttribute("error", "Event with this name couldn't be found. Do you want to create it?");
            }
        }
        return "admin/events/event";
    }

    @GetMapping("/events/delete/{id}")
    public RedirectView deleteEvent(RedirectAttributes attributes,
                                    @PathVariable(value = "id") long id) {
        eventService.delete(id);
        attributes.addFlashAttribute("success", "The event was deleted successfully");
        return new RedirectView("/admin/events");
    }

    @PostMapping(path = "events/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RedirectView saveEvents(Principal principal,
                                   RedirectAttributes attributes,
                                   @PathVariable(value = "id") long id,
                                   @RequestPart("image") MultipartFile image,
                                   @RequestParam("title") String title,
                                   @RequestParam("content") String content,
                                   @RequestParam("place") String place,
                                   @RequestParam("status") String status,
                                   @RequestParam("startDate") String startDate,
                                   @RequestParam("endDate") String endDate
    ) throws ParseException {
        Optional<Event> oldRecord = eventService.getEventById(id);
        Event event = oldRecord.isPresent() ? oldRecord.get() : new Event();
        if (image.getSize() > 0) {
            try {
                event.setPhotoUrl(fileStorageService.save(image, String.format("event_%d", id)));
            } catch (IOException e) {
                attributes.addFlashAttribute("error", "An error occured while saving the image");
            }
        }
        event.setStatus(status);
        event.setTitle(title);
        event.setContent(content);
        event.setPlace(place);
        event.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
        event.setEndDate(new SimpleDateFormat("yyyy-MM-dd").parse(endDate));
        event.setEditedBy(userService.getUser(principal.getName()));
        event.setEdited(new Date());
        eventService.save(event);
        attributes.addFlashAttribute("success", "Event saved successfully");
        return new RedirectView("/admin/events/" + event.getId());
    }

    //endregion
    //region <News>
    @GetMapping("/news")
    public String getNews(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        List<NewsItem> news = newsService.getNews();
        model.addAttribute("success", success);
        model.addAttribute("editPath", "/admin/news/{id}");
        model.addAttribute("deletePath", "/admin/news/delete/{id}");
        model.addAttribute("error", error);
        model.addAttribute("news", news);
        return "admin/news/index";
    }

    @GetMapping("/news/{id}")
    public String getNews(Model model, @PathVariable(value = "id") long id, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        Optional<NewsItem> news = newsService.getNewsById(id);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        if (news.isPresent()) {
            model.addAttribute("news", news.get());
        } else {
            if (id == 0) {
                NewsItem newNews = new NewsItem();
                model.addAttribute("news", newNews);
            } else {
                model.addAttribute("error", "An article with this name couldn't be found. Do you want to create it?");
            }
        }
        return "admin/news/news";
    }

    @GetMapping("/news/delete/{id}")
    public RedirectView deleteNews(RedirectAttributes attributes,
                                   @PathVariable(value = "id") long id) {
        newsService.deleteNews(id);
        attributes.addFlashAttribute("success", "The news article was deleted successfully");
        return new RedirectView("/admin/news");
    }

    @PostMapping(path = "news/{id}")
    public RedirectView saveNews(Principal principal,
                                 RedirectAttributes attributes,
                                 @PathVariable(value = "id") long id,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content,
                                 @RequestParam("date") String date
    ) throws ParseException {
        Optional<NewsItem> oldRecord = newsService.getNewsById(id);
        NewsItem news = oldRecord.isPresent() ? oldRecord.get() : new NewsItem();
        news.setTitle(title);
        news.setContent(content);
        news.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        news.setEditedBy(userService.getUser(principal.getName()));
        news.setEdited(new Date());
        newsService.save(news);
        attributes.addFlashAttribute("success", "Event saved successfully");
        return new RedirectView("/admin/news/" + news.getId());
    }

    //endregion
    //region <Album>
    @GetMapping("/albums")
    public String getAlbums(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        List<Album> albums = albumService.getAlbums();
        model.addAttribute("success", success);
        model.addAttribute("editPath", "/admin/albums/{id}");
        model.addAttribute("deletePath", "/admin/albums/delete/{id}");
        model.addAttribute("error", error);
        model.addAttribute("albums", albums);
        return "admin/albums/index";
    }

    @GetMapping("/albums/{id}")
    public String getAlbum(Model model, @PathVariable(value = "id") long id, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        Optional<Album> album = albumService.getAlbumById(id);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        if (album.isPresent()) {
            model.addAttribute("album", album.get());
        } else {
            if (id == 0) {
                Album newAlbum = new Album();
                model.addAttribute("album", newAlbum);
            } else {
                model.addAttribute("error", "Album with this name couldn't be found. Do you want to create it?");
            }
        }
        return "admin/albums/album";
    }

    @GetMapping("/albums/delete/{id}")
    public RedirectView deleteAlbum(RedirectAttributes attributes,
                                    @PathVariable(value = "id") long id) {
        albumService.delete(id);
        attributes.addFlashAttribute("success", "The album was deleted successfully");
        return new RedirectView("/admin/albums");
    }

    @PostMapping(path = "albums/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public RedirectView saveAlbums(Principal principal,
                                   RedirectAttributes attributes,
                                   @PathVariable(value = "id") long id,
                                   @RequestPart("image") MultipartFile image,
                                   @RequestParam("title") String title,
                                   @RequestParam("shortDesc") String shortDesc,
                                   @RequestParam("longDesc") String longDesc
    ) throws ParseException {
        Optional<Album> oldRecord = albumService.getAlbumById(id);
        Album album = oldRecord.isPresent() ? oldRecord.get() : new Album();
        if (image.getSize() > 0) {
            try {
                album.setPhotoPath(fileStorageService.save(image, String.format("album_%d", id)));
            } catch (IOException e) {
                attributes.addFlashAttribute("error", "An error occured while saving the image");
            }
        }
        album.setTitle(title);
        album.setShortDesc(shortDesc);
        album.setLongDesc(longDesc);
        String code = String.format("%s-%d", title.replaceAll("\\s+",""), id);
        album.setCode(code);
        album.setEditedBy(userService.getUser(principal.getName()));
        album.setEdited(new Date());
        albumService.save(album);
        attributes.addFlashAttribute("success", "Album saved successfully");
        return new RedirectView("/admin/albums/" + album.getId());
    }

    //endregion
    //region <Subpages>
    @GetMapping("/subpages")
    public String getSubpages(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "admin/subpage/index";
    }

    @GetMapping("/subpages/{subpage}")
    public String getSpecificSubpage(Model model, @PathVariable(value = "subpage") String subpage, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "admin/subpage/subpage";
    }

    //endregion
    //region <Social media>
    @GetMapping("/social")
    public String getSocialMedia(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        Map<String, String> items = itemService.getItemsByPage(Arrays.asList("socialMedia", "common"));
        model.addAttribute("items", items);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        model.addAttribute("items", items);
        return "admin/social";
    }

    @PostMapping("/social")
    public RedirectView saveSocialMedia(RedirectAttributes attributes, @RequestParam("facebook") String facebook, @RequestParam("twitter") String twitter, @RequestParam("instagram") String instagram) {
        attributes.addFlashAttribute("success", "Data saved successfully");
        itemService.saveItem("facebook", facebook);
        itemService.saveItem("twitter", twitter);
        itemService.saveItem("instagram", instagram);
        return new RedirectView("/admin/social");
    }

    //endregion
    //region <Settings>
    @GetMapping("/settings")
    public String getSettings(Model model, @ModelAttribute("success") String success, @ModelAttribute("error") String error) {
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        return "admin/settings";
    }

    @PostMapping("/changePassword")
    public RedirectView changePassword(RedirectAttributes attributes, @RequestParam("currentPass") String currentPass,
                                       @RequestParam("newPass") String newPass,
                                       @RequestParam("newPass2") String newPass2,
                                       Principal principal) {
        switch (userService.changePassword(currentPass, newPass, newPass2, principal.getName())) {
            case -1:
                attributes.addFlashAttribute("error", "Passwords don't match");
                break;
            case -2:
                attributes.addFlashAttribute("error", "Current password doesn't match");
                break;
            default:
                attributes.addFlashAttribute("success", "Password changed successfully");
                break;
        }
        return new RedirectView("/admin/settings");
    }

    @GetMapping("/logout")
    public RedirectView logout() {
        return new RedirectView("/");
    }
    //endregion
}
