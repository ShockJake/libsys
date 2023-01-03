package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Post.PostService;
import com.university.libsys.backend.services.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostService postService;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(PostsController.class);

    @Autowired
    public PostsController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping("/{id}")
    public String postPage(@PathVariable Long id, Model model) throws PostNotFoundException {
        final Post post = postService.getPostById(id);
        model.addAttribute("postHeader", post.getPostHeader());
        model.addAttribute("postText", post.getPostText());
        model.addAttribute("postPhotoPath", post.getPostPhotoPath());
        return "pages/postTemplate";
    }

    @ModelAttribute
    public Post getEmptyPost(Authentication authentication) throws UserNotFoundException {
        final Post post = new Post();
        post.setPostID(null);
        post.setPostPhotoPath(null);
        post.setWriterID(userService.getUserByLogin(authentication.getName()).getUserID());
        return post;
    }

    @RequestMapping("/create_post")
    public String createPostPage() {
        return "pages/create_post";
    }

    @PutMapping("/create_post")
    public String createPost(@Valid Post post, Model model, @RequestParam MultipartFile postPhotoPath) {
        try {
            log.info(String.format("Received file: %s", postPhotoPath.getOriginalFilename()));
            postService.validatePost(post);

            final Post savedPost = postService.saveNewPost(post);
            final List<String> messages = List.of(String.format("Post ID: %s", savedPost.getPostID()),
                    String.format("Writer: %s", userService.getUserById(savedPost.getWriterID()).getName()));

            model.addAttribute("infoHeader", String.format("Created post %s", savedPost.getPostHeader()));
            model.addAttribute("infoMessages", messages);
            return "infoPage";
        } catch (ValidationException | UserNotFoundException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "pages/create_post";
        }
    }
}
