package com.university.libsys.backend.services.Request;

import com.university.libsys.backend.entities.LibsysRequest;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.RequestNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.RequestRepository;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.backend.utils.RequestStatus;
import com.university.libsys.backend.utils.RequestType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibsysRequestServiceImpl implements LibsysRequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;

    @Autowired
    public LibsysRequestServiceImpl(RequestRepository requestRepository, UserService userService) {
        this.requestRepository = requestRepository;
        this.userService = userService;
    }

    @Override
    public LibsysRequest getRequestByID(@NotNull Long id) throws RequestNotFoundException {
        return requestRepository.findById(id).orElseThrow(() -> new RequestNotFoundException(id));
    }

    @Override
    public List<LibsysRequest> getAllRequests() {
        return requestRepository.findAll();
    }

    @Override
    public List<LibsysRequest> getRequestsForUser(@NotNull Long id) {
        return requestRepository.findLibsysRequestByRequestSenderID(id);
    }

    @Override
    public LibsysRequest approveRequest(@NotNull Long id) throws RequestNotFoundException {
        // todo: implement approving in system
        final LibsysRequest requestToApprove = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
        requestToApprove.setRequestStatus(RequestStatus.APPROVED);
        return requestRepository.save(requestToApprove);
    }

    @Override
    public LibsysRequest rejectRequest(@NotNull Long id) throws RequestNotFoundException {
        // todo: implement rejecting in system
        final LibsysRequest requestToReject = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
        requestToReject.setRequestStatus(RequestStatus.REJECTED);
        return requestRepository.save(requestToReject);
    }

    @Override
    public LibsysRequest deleteRequest(@NotNull Long id) throws RequestNotFoundException {
        final LibsysRequest requestToDelete = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
        requestRepository.delete(requestToDelete);
        return requestToDelete;
    }

    @Override
    public LibsysRequest createRequest(@NotNull String login, @NotNull String type) throws UserNotFoundException {
        final User user = userService.getUserByLogin(login);
        final LibsysRequest request = new LibsysRequest(null, user.getUserID(), RequestType.valueOf(type), RequestStatus.PENDING);
        return requestRepository.save(request);
    }
}
