package com.university.libsys.web.controllers;

import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Post.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    private final PostService postService;

    public WebController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping("/")
    public String indexPage(Model model, Authentication authentication) throws UserNotFoundException {
        if (authentication != null) {
            model.addAttribute("posts", postService.getAllPostsOrderedByTime(authentication.getName()));
        } else {
            model.addAttribute("posts", postService.getAllPostsOrderedByTime());
        }
        return "index";
    }

    @RequestMapping("/libraries")
    public String librariesPage() {
        return "pages/public_pages/library_search";
    }

    @RequestMapping("/books")
    public String booksPage() {
        return "pages/public_pages/book_search";
    }

    @RequestMapping("/selections")
    public String selectionsPage() {
        return "pages/public_pages/book_selections";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        return "pages/public_pages/about";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

    @RequestMapping("/info_page")
    public String infoPage(Model model) {
        return "/infoPage";
    }
}
