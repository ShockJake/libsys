package com.university.libsys.web.formatters;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.User.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class UserIdFormatter implements Formatter<Long> {

    private final UserService userService;

    @Autowired
    public UserIdFormatter(UserService userService) {
        this.userService = userService;
    }


    @Override
    public @NotNull Long parse(@NotNull String text, @NotNull Locale locale) throws ParseException {
        return Long.parseLong(text);
    }

    @Override
    public @NotNull String print(@NotNull Long object, @NotNull Locale locale) {
        try {
            final User user = userService.getUserById(object);
            return user.getLogin();
        } catch (UserNotFoundException e) {
            return object.toString();
        }
    }
}
