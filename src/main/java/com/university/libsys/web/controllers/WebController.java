package com.university.libsys.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {

    @RequestMapping("/")
    public String indexPage() {
        return "index";
    }

    @RequestMapping("/libraries")
    public String librariesPage() {
        return "pages/library_search";
    }

    @RequestMapping("/books")
    public String booksPage() {
        return "pages/book_search";
    }

    @RequestMapping("/selections")
    public String selectionsPage() {
        return "pages/book_selections";
    }

    @RequestMapping("/about")
    public String aboutPage() {
        return "pages/about";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }

}
