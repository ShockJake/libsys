package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.User;
import com.university.libsys.utils.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AccountController {

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
        user.setName("Fancy name");
        user.setUserRole(UserRole.READER);
        return user;
    }

    @PostMapping("/createAccount")
    public String saveUser(User user) {
        // saving works
        //todo: add saving to repo and result page
        return "index";
    }
}
