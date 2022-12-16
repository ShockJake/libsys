package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AccountController {

    private final UserService userService;

    @Autowired
    AccountController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/createAccount")
    public String createAccountPage() {
        return "createAccount";
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        model.addAttribute("userName", authentication.getName());
        return "pages/account";
    }

    @ModelAttribute
    public User getUser() {
        final User user = new User();
        user.setUserID(null);
        user.setUserRole(UserRole.READER);
        return user;
    }

    @PostMapping("/createAccount")
    public String saveUser(User user, Model model) throws AlreadyExistingUserException {
//        System.out.println(user);
        final User addedUser = userService.saveNewUser(user);
        final List<String> messages = List.of(
                String.format("Login: %s", addedUser.getLogin()),
                String.format("Name: %s", addedUser.getName()),
                String.format("Your role: %s", addedUser.getUserRole().name())
        );
        model.addAttribute("infoHeader", "Account is created successfully");
        model.addAttribute("infoMessages", messages);
        return "infoPage";
    }
}
