package com.university.libsys.backend.services.Request;

import com.university.libsys.backend.entities.LibsysRequest;
import com.university.libsys.backend.exceptions.AlreadyProcessedRequestException;
import com.university.libsys.backend.exceptions.RequestNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LibsysRequestService {

    LibsysRequest getRequestByID(@NotNull Long id) throws RequestNotFoundException;

    List<LibsysRequest> getAllRequests();

    LibsysRequest approveRequest(@NotNull Long id) throws RequestNotFoundException, UserNotFoundException, AlreadyProcessedRequestException;

    LibsysRequest rejectRequest(@NotNull Long id) throws RequestNotFoundException, UserNotFoundException, AlreadyProcessedRequestException;

    LibsysRequest deleteRequest(@NotNull Long id) throws RequestNotFoundException;

    LibsysRequest createRequest(@NotNull String login, @NotNull String type) throws UserNotFoundException;
}
