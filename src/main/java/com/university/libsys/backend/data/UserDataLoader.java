package com.university.libsys.backend.data;

import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.repositories.UserRepository;
import com.university.libsys.backend.utils.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

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
        if (userRepository.count() == 0) {
            // save functional users only if they are not present in database
            log.warn("Inserting functional users to database as it's empty");
            List<User> users = List.of(
                    new User(null, "admin", "admin", "SuperAdmin", UserRole.ADMIN, 1),
                    new User(null, "writer", "1234", "Writer007", UserRole.WRITER, 0),
                    new User(null, "reader", "1111", "ReaderMan", UserRole.READER, 0)
            );
            userRepository.saveAll(users);
        }
    }
}
