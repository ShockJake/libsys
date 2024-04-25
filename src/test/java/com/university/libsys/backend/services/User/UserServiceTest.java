package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.UserRepository;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.utils.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final MessageService messageService = Mockito.mock(MessageService.class);
    private final UserService userService = new UserServiceImpl(userRepository, messageService);
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
    private final String password = passwordEncoder.encode("dummyPassword");

    @BeforeAll
    static void initialize() {

    }

    @Test
    void shouldGetUserById() {
        // given
        final long id = 1;
        given(userRepository.findById(id)).willReturn(Optional.of(getTestUser()));

        // when
        final AtomicReference<User> user = new AtomicReference<>();
        assertDoesNotThrow(() -> user.set(userService.getUserById(id)));

        // then
        assertNotNull(user.get());
        assertEquals(id, user.get().getUserID());
    }

    @Test
    void shouldNotGetUserById() {
        // given
        final long nonExistingId = -1;
        given(userRepository.findById(nonExistingId)).willReturn(Optional.empty());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(nonExistingId));
    }

    @Test
    void shouldGetUserByLogin() {
        // given
        final String login = "testLogin";
        given(userRepository.findUserByLogin(login)).willReturn(getTestUser());

        // when
        final AtomicReference<User> user = new AtomicReference<>();
        assertDoesNotThrow(() -> user.set(userService.getUserByLogin(login)));

        // then
        assertEquals(login, user.get().getLogin());
    }

    @Test
    void shouldNotGetUserByLogin() {
        // given
        final String nonExistingLogin = "NON_EXISTING_LOGIN";
        given(userRepository.findUserByLogin(nonExistingLogin)).willReturn(null);

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.getUserByLogin(nonExistingLogin));
    }

    @Test
    void shouldSaveNewUser() {
        // given
        final User userToSave = getTestUser();
        given(userRepository.save(userToSave)).willReturn(getTestUser());
        given(userRepository.findUserByLogin(userToSave.getLogin())).willReturn(null);

        // when
        final AtomicReference<User> user = new AtomicReference<>();
        assertDoesNotThrow(() -> user.set(userService.saveNewUser(userToSave)));

        // then
        assertEquals(userToSave.getUserRole(), user.get().getUserRole());
        assertEquals(userToSave.getUserID(), user.get().getUserID());
        assertEquals(userToSave.getName(), user.get().getName());
        assertEquals(userToSave.getLogin(), user.get().getLogin());
        assertEquals(userToSave.getPostsNumber(), user.get().getPostsNumber());
    }

    @Test
    void shouldNotSaveNewUserAsItIsAlreadyExists() {
        // given
        final User alreadyExistingUser = getTestUser();
        given(userRepository.findUserByLogin(alreadyExistingUser.getLogin())).willReturn(getTestUser());
        given(userRepository.save(alreadyExistingUser)).willReturn(getTestUser());

        // when & then
        assertThrows(AlreadyExistingUserException.class, () -> userService.saveNewUser(alreadyExistingUser));
    }

    @Test
    void shouldDeleteUser() {
        // given
        final User userToDelete = getTestUser();
        given(userRepository.findById(userToDelete.getUserID())).willReturn(Optional.of(getTestUser()));
        doNothing().when(userRepository).deleteById(userToDelete.getUserID());

        // when
        final AtomicReference<User> user = new AtomicReference<>();
        assertDoesNotThrow(() -> user.set(userService.deleteUser(userToDelete.getUserID())));

        // then
        assertEquals(userToDelete, user.get());
    }

    @Test
    void shouldNotDeleteUserAsItIsNotExists() {
        // given
        final User userToDelete = getTestUser();
        given(userRepository.findUserByLogin(userToDelete.getLogin())).willReturn(null);
        doNothing().when(userRepository).deleteById(userToDelete.getUserID());

        // when & then
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(userToDelete.getUserID()));
    }

    @Test
    void shouldValidateUser() {
        // given
        final User userToValidate = getTestUser();

        // when & then
        assertDoesNotThrow(() -> userService.validateUser(userToValidate));
    }

    @Test
    void shouldNotValidateUser() {
        // given
        final User userToValidate1 = new User();
        final User userToValidate2 = new User(null, "", "", "", UserRole.READER, 0);

        // when & then
        assertThrows(ValidationException.class, () -> userService.validateUser(userToValidate1));
        assertThrows(ValidationException.class, () -> userService.validateUser(userToValidate2));
    }

    @Test
    void shouldUpdateUserRole() {
        // given
        final Long id = getTestUser().getUserID();
        final UserRole newUserRole = UserRole.ADMIN;
        final User updatedUser = getTestUser();
        updatedUser.setUserRole(newUserRole);
        given(userRepository.findById(id)).willReturn(Optional.of(getTestUser()));
        given(userRepository.save(updatedUser)).willReturn(updatedUser);

        // when
        final AtomicReference<User> user = new AtomicReference<>();
        assertDoesNotThrow(() -> user.set(userService.updateUser(id, updatedUser)));

        // then
        assertEquals(updatedUser, user.get());
    }

    private User getTestUser() {
        return new User(1L, "testLogin", password, "TEST", UserRole.READER, 0);
    }
}