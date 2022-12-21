package com.university.libsys.web.controllers;

import com.university.libsys.backend.services.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/administration")
@Controller
public class AdministrationController {

    private final UserService userService;

    @Autowired
    public AdministrationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/users")
    public String userManagerPage(Model model) {

        model.addAttribute("users", userService.getAllUsers());
        return "pages/userManaging";
    }
}
