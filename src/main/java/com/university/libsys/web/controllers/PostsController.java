package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.services.Post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostService postService;

    @Autowired
    public PostsController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping("/{id}")
    public String postPage(@PathVariable Long id, Model model) throws PostNotFoundException {
        final Post post = postService.getPostById(id);
        model.addAttribute("postHeader", post.getPostHeader());
        model.addAttribute("postText", post.getPostText());
        model.addAttribute("postPhotoPath", post.getPostPhotoPath());
        return "/pages/postTemplate";
    }
}
