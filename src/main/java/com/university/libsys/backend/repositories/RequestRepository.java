package com.university.libsys.backend.repositories;

import com.university.libsys.backend.entities.LibsysRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<LibsysRequest, Long> {

    List<LibsysRequest> findLibsysRequestByRequestSenderID(@NotNull Long id);
}
