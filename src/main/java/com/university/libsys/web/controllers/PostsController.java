package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.Post;
import com.university.libsys.backend.exceptions.PostNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.File.FileService;
import com.university.libsys.backend.services.Post.PostService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.web.util.ModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/posts")
public class PostsController {

    private final PostService postService;
    private final UserService userService;
    private final FileService fileService;
    private final Logger log = LoggerFactory.getLogger(PostsController.class);

    @Autowired
    public PostsController(PostService postService, UserService userService, FileService fileService) {
        this.postService = postService;
        this.userService = userService;
        this.fileService = fileService;
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

    @PostMapping("/create_post")
    public String createPost(@Valid Post post, Errors errors, Model model, @RequestParam("postPhotoPath") MultipartFile postPhoto) {
        if (errors.hasErrors()) {
            final String errorMessages = getAllErrors(errors);
            log.error(errorMessages);
            ModelUtil.fillWithError(model, errorMessages);
            return "pages/create_post";
        }

        try {
            log.info(String.format("Received file: %s", postPhoto.getOriginalFilename()));
            postService.validatePost(post);

            final Post savedPost = postService.saveNewPost(post);
            fileService.save(Objects.requireNonNull(postPhoto.getOriginalFilename()), postPhoto.getInputStream());

            final List<String> messages = List.of(String.format("Post ID: %s", savedPost.getPostID()),
                    String.format("Writer: %s", userService.getUserById(savedPost.getWriterID()).getName()));
            ModelUtil.fillInfoModelWithArguments(model, String.format("Created post %s", savedPost.getPostHeader()), messages);

            return "infoPage";
        } catch (ValidationException | UserNotFoundException | IOException e) {
            log.error(e.getMessage());
            ModelUtil.fillWithError(model, e.getMessage());
            return "pages/create_post";
        }
    }

    private String getAllErrors(Errors errors) {
        final StringBuilder builder = new StringBuilder();
        errors.getAllErrors().forEach(objectError -> {
            builder.append(objectError.getDefaultMessage());
            builder.append('\n');
        });
        return builder.toString();
    }
}
