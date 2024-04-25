package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.services.Post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private final Logger log = LoggerFactory.getLogger(SearchController.class);
    private final PostService postService;

    public SearchController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/search")
    public String searchPage(@RequestParam String prompt, Model model) {
        log.info("Got a search request with prompt: {}", prompt);
        final List<Post> result = postService.getPostsBySearchPrompt(prompt);
        log.info("Search results number for prompt '{}' are - {}", prompt, result.size());
        model.addAttribute("posts", result);
        return "search";
    }
}
