package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import org.jetbrains.annotations.NotNull;

import javax.validation.ValidationException;
import java.util.List;

public interface UserService {

    User getUserById(@NotNull Long id) throws UserNotFoundException;

    User getUserByLogin(@NotNull String login) throws UserNotFoundException;

    List<User> getAllUsers();

    User saveNewUser(@NotNull User user) throws AlreadyExistingUserException;

    User deleteUser(@NotNull User user) throws UserNotFoundException;

    void validateUser(@NotNull User user) throws ValidationException;
}
