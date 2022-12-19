package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.validation.ValidationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserById(@NotNull Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserByLogin(@NotNull String login) throws UserNotFoundException {
        return Optional.ofNullable(userRepository.findUserByLogin(login))
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    @Override
    public User saveNewUser(@NotNull User userToSave) throws AlreadyExistingUserException {
        final User user = userRepository.findUserByLogin(userToSave.getLogin());
        if (user != null) {
            throw new AlreadyExistingUserException(userToSave.getLogin());
        }
        return userRepository.save(userToSave);
    }

    @Override
    public User deleteUser(@NotNull User userToDelete) throws UserNotFoundException {
        Optional.ofNullable(userRepository.findUserByLogin(userToDelete.getLogin()))
                .orElseThrow(() -> new UserNotFoundException(userToDelete.getLogin()));
        userRepository.deleteById(userToDelete.getUserID());
        return userToDelete;
    }

    @Override
    public void validateUser(@NotNull User user) throws ValidationException {
        final Map<String, String> userFields = new LinkedHashMap<>();
        userFields.put("login", user.getLogin());
        userFields.put("password", user.getPassword());
        userFields.put("name", user.getName());

        userFields.entrySet().stream()
                .map(entry -> {
                    String value = Optional.ofNullable(entry.getValue()).orElseThrow(() ->
                            new ValidationException(String.format("Field (%s) cannot be null", entry.getKey())));
                    return Map.entry(entry.getKey(), value);
                })
                .forEach(entry -> {
                    if (StringUtils.isEmptyOrWhitespace(entry.getValue())) {
                        throw new ValidationException(String.format("Field (%s) cannot be blank", entry.getKey()));
                    }
                });
    }
}
