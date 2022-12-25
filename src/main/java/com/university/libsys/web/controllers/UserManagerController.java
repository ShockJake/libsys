package com.university.libsys.web.controllers;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.utils.UserRole;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_management")
public class UserManagerController {

    private final UserService userService;

    public UserManagerController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestParam("name") String name, @RequestParam("login") String login, @RequestParam("userRole") String userRole) throws UserNotFoundException {
        final User userToUpdate = getUpdatedUser(name, login, userRole);
        return userService.updateUser(id, userToUpdate);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable(name = "id") Long id) throws UserNotFoundException {
        return userService.deleteUser(userService.getUserById(id));
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }

    private User getUpdatedUser(String name, String login, String userRole) {
        final User user = new User();
        user.setName(name);
        user.setLogin(login);
        user.setUserRole(UserRole.valueOf(userRole));
        return user;
    }
}
