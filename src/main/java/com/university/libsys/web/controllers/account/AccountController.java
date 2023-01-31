package com.university.libsys.web.controllers.account;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.backend.utils.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final MessageService messageService;

    @Autowired
    AccountController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @GetMapping("/account")
    public String accountPage(Model model, Authentication authentication) {
        final User user;
        try {
            user = userService.getUserByLogin(authentication.getName());
        } catch (UserNotFoundException e) {
            model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/customErrorPage";
        }
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
            final User addedUser = userService.saveNewUser(user);
            model.addAttribute("infoHeader", "Account is created successfully");
            model.addAttribute("infoMessages", List.of(
                    String.format("Login: %s", addedUser.getLogin()),
                    String.format("Name: %s", addedUser.getName()),
                    String.format("Your role: %s", addedUser.getUserRole().name()),
                    "Please now login into your account"));
            return "infoPage";
        } catch (AlreadyExistingUserException | ValidationException e) {
            model.addAttribute("error", e.getMessage());
            return "/createAccount";
        }
    }

    @GetMapping("/messages")
    public String messagesPage(Model model, Authentication authentication) {
        final User user;
        try {
            user = userService.getUserByLogin(authentication.getName());
        } catch (UserNotFoundException e) {
            model.addAttribute("errorCode", HttpStatus.NOT_FOUND.value());
            model.addAttribute("errorMessage", e.getMessage());
            return "pages/customErrorPage";
        }
        final List<Message> messages = messageService.getMessagesByReceiverID(user.getUserID());

        model.addAttribute("messages", messages);
        return "pages/messages/messagesPage";
    }
}
