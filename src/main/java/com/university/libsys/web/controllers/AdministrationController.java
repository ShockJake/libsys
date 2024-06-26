package com.university.libsys.web.controllers;

import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Post.PostService;
import com.university.libsys.backend.services.Request.LibsysRequestService;
import com.university.libsys.backend.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/administration")
@Controller
public class AdministrationController {

    private final UserService userService;
    private final PostService postService;
    private final LibsysRequestService requestService;

    @Autowired
    public AdministrationController(UserService userService, PostService postService, LibsysRequestService requestService) {
        this.userService = userService;
        this.postService = postService;
        this.requestService = requestService;
    }

    @RequestMapping("/users")
    public String userManagerPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "pages/management/userManagerPage";
    }

    @RequestMapping("/posts")
    public String postsManagerPage(Model model, Authentication authentication) {
        try {
            model.addAttribute("posts", postService.getAllPosts(authentication.getName()));
        } catch (UserNotFoundException e) {
            model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/customErrorPage";
        }
        return "pages/management/postsManagerPage";
    }

    @RequestMapping("/requests")
    public String requestsManagerPage(Model model) {
        model.addAttribute("requests", requestService.getAllRequests());
        return "pages/management/requestManagerPage";
    }
}
