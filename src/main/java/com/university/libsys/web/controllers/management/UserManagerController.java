package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.User.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_management")
public class UserManagerController {

    private final UserService userService;

    public UserManagerController(UserService userService) {
        this.userService = userService;
    }

    @PatchMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) throws UserNotFoundException {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws UserNotFoundException {
        return userService.getUserById(id);
    }
}
