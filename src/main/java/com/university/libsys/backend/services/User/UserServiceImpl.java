package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.validation.ValidationException;
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
    public User saveNewUser(@NotNull User user) throws AlreadyExistingUserException {
        Example<User> userExample = Example.of(user);
        if (userRepository.exists(userExample)) {
            throw new AlreadyExistingUserException(user.getLogin());
        }
        return userRepository.save(user);
    }

    @Override
    public void validateUser(@NotNull User user) throws ValidationException {
        Map<String, String> userFields =
                Map.of("login", user.getLogin(), "name", user.getName(), "password", user.getPassword());
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
