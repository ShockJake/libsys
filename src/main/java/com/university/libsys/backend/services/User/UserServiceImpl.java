package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.UserRepository;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.utils.ValidationUtil;
import com.university.libsys.web.util.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
    private final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, MessageService messageService) {
        this.userRepository = userRepository;
        this.messageService = messageService;
    }

    @Override
    public User getUserById(@NotNull Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User getUserByLogin(@NotNull String login) throws UserNotFoundException {
        log.debug("Getting user by login - {}", login);
        return Optional.ofNullable(userRepository.findUserByLogin(login))
                .orElseThrow(() -> new UserNotFoundException(login));
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Getting all users");
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User saveNewUser(@NotNull User userToSave) throws AlreadyExistingUserException {
        if (userRepository.findUserByLogin(userToSave.getLogin()) != null) {
            throw new AlreadyExistingUserException(userToSave.getLogin());
        }
        validateUser(userToSave);
        userToSave.setPassword(passwordEncoder.encode(userToSave.getPassword()));
        final User savedUser = userRepository.save(userToSave);
        messageService.saveNewMessage(MessageUtil.getCreatedAccountMessage(savedUser.getUserID()));
        log.debug("Inserted new user - {}", userToSave.getLogin());
        return savedUser;
    }

    @Override
    @Transactional
    public User deleteUser(@NotNull Long id) throws UserNotFoundException {
        final User userToDelete = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
        messageService.deleteMessagesForUser(id);
        log.debug("Deleted user - {}", userToDelete.getLogin());
        return userToDelete;
    }

    @Override
    @Transactional
    public User updateUser(@NotNull Long id, @NotNull User userToUpdate) throws UserNotFoundException {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.debug("Updating user {}", user.getLogin());
        log.debug("Old values: {}, {}, {}", user.getLogin(), user.getName(), user.getUserRole().name());
        log.debug("New values: {}, {}, {}", userToUpdate.getLogin(), userToUpdate.getName(), userToUpdate.getUserRole().name());
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
        if (!StringUtils.equals(userToSave.getLogin(), userToUpdate.getLogin())) {
            userToSave.setLogin(userToUpdate.getLogin());
        }
        if (!userToSave.getUserRole().equals(userToUpdate.getUserRole())) {
            userToSave.setUserRole(userToUpdate.getUserRole());
        }
        if (!StringUtils.equals(userToSave.getName(), userToUpdate.getName())) {
            userToSave.setName(userToUpdate.getName());
        }
        if (!userToSave.getPostsNumber().equals(userToUpdate.getPostsNumber())) {
            userToSave.setPostsNumber(userToUpdate.getPostsNumber());
        }
    }
}
