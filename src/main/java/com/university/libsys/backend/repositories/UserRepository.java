package com.university.libsys.backend.repositories;

import com.university.libsys.backend.entities.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByLogin(@NotNull String login);
}
