package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.backend.utils.UserRole;
import com.university.libsys.web.util.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Controller
public class AccountController {

    private final UserService userService;

    @Autowired
    AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) throws UserNotFoundException {
        final User user = userService.getUserByLogin(authentication.getName());
        model.addAttribute("userName", user.getName());
        model.addAttribute("userRole", user.getUserRole().name());
        model.addAttribute("userLogin", user.getLogin());
        return "pages/account";
    }

    @ModelAttribute
    public User getUser() {
        final User user = new User();
        user.setUserID(null);
        user.setUserRole(UserRole.READER);
        user.setPostsNumber(0);
        return user;
    }

    @RequestMapping("/createAccount")
    public String createAccountPage() {
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String saveUser(@Valid User user, Model model) {
        try {
            userService.validateUser(user);
            final User addedUser = userService.saveNewUser(user);

            final List<String> messages = List.of(
                    String.format("Login: %s", addedUser.getLogin()),
                    String.format("Name: %s", addedUser.getName()),
                    String.format("Your role: %s", addedUser.getUserRole().name()),
                    "Please now login into your account");
            ModelUtil.fillInfoModelWithArguments(model, "Account is created successfully", messages);

            return "/infoPage";
        } catch (AlreadyExistingUserException | ValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "/createAccount";
        }
    }
}
