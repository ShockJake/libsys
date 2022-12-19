package com.university.libsys.backend.services.User;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.UserRepository;
import com.university.libsys.utils.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private final UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository);

    @BeforeAll
    static void initialize() {

    }

    @Test
    void shouldGetUserById() {
        // given
        final long id = 1;
        given(userRepository.findById(id)).willReturn(Optional.of(getTestUser()));

        // when
        AtomicReference<User> user = new AtomicReference<>();
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
    void getUserByLogin() {
        // given
        final String login = "testLogin";
        given(userRepository.findUserByLogin(login)).willReturn(getTestUser());

        // when
        AtomicReference<User> user = new AtomicReference<>();
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
    void saveNewUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void validateUser() {
    }

    private User getTestUser() {
        return new User(1L, "testLogin", "dummyPassword", "TEST", UserRole.READER);
    }
}