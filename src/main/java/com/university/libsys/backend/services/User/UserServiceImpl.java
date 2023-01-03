package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.UserRepository;
import com.university.libsys.backend.utils.ValidationUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User saveNewUser(@NotNull User userToSave) throws AlreadyExistingUserException {
        final User user = userRepository.findUserByLogin(userToSave.getLogin());
        if (user != null) {
            throw new AlreadyExistingUserException(userToSave.getLogin());
        }
        log.debug(String.format("Inserter new user - %s", userToSave.getLogin()));
        return userRepository.save(userToSave);
    }

    @Override
    public User deleteUser(@NotNull User userToDelete) throws UserNotFoundException {
        Optional.ofNullable(userRepository.findUserByLogin(userToDelete.getLogin()))
                .orElseThrow(() -> new UserNotFoundException(userToDelete.getLogin()));
        userRepository.deleteById(userToDelete.getUserID());
        log.debug(String.format("Deleted user - %s", userToDelete.getLogin()));
        return userToDelete;
    }

    @Override
    public User updateUser(@NotNull Long id, @NotNull User userToUpdate) throws UserNotFoundException {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.debug(String.format("Updating user %s", user.getLogin()));
        log.debug(String.format("Old values: %s, %s, %s",
                user.getLogin(), user.getName(), user.getUserRole().name()));
        log.debug(String.format("New values: %s, %s, %s",
                userToUpdate.getLogin(), userToUpdate.getName(), userToUpdate.getUserRole().name()));
        updateUser(userToUpdate, user);
        return userRepository.save(user);
    }

    @Override
    public void validateUser(@NotNull User user) throws ValidationException {
        final Map<String, String> userFields = new LinkedHashMap<>();
        userFields.put("login", user.getLogin());
        userFields.put("password", user.getPassword());
        userFields.put("name", user.getName());
        ValidationUtil.validateFields(userFields);
    }

    private void updateUser(User userToUpdate, User userToSave) {
        if (!userToSave.getLogin().equals(userToUpdate.getLogin())) {
            userToSave.setLogin(userToUpdate.getLogin());
        }
        if (!userToSave.getUserRole().equals(userToUpdate.getUserRole())) {
            userToSave.setUserRole(userToUpdate.getUserRole());
        }
        if (!userToSave.getName().equals(userToUpdate.getName())) {
            userToSave.setName(userToUpdate.getName());
        }
    }
}
