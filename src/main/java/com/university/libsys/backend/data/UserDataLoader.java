package com.university.libsys.backend.data;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.university.libsys.backend.utils.UserRole.*;

@Component
public class UserDataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(UserDataLoader.class);

    @Autowired
    UserDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        final String adminPassword = passwordEncoder.encode("admin");
        final String writerPassword = passwordEncoder.encode("1234");
        final String readerPassword = passwordEncoder.encode("1111");

        if (userRepository.count() == 0) {
            // save functional users only if they are not present in database
            log.warn("Inserting functional users to database as it's empty");
            List<User> users = List.of(
                    new User(null, "admin",  adminPassword, "SuperAdmin", ADMIN, 1),
                    new User(null, "writer", writerPassword, "Writer007", WRITER, 0),
                    new User(null, "reader", readerPassword, "ReaderMan", READER, 0)
            );
            userRepository.saveAll(users);
        }
    }
}
