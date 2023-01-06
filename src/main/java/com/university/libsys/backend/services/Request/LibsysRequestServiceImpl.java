package com.university.libsys.backend.services.Request;

import com.university.libsys.backend.entities.LibsysRequest;
import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.entities.User;
import com.university.libsys.backend.exceptions.AlreadyProcessedRequestException;
import com.university.libsys.backend.exceptions.RequestNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.repositories.RequestRepository;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.backend.utils.MessageStatus;
import com.university.libsys.backend.utils.RequestStatus;
import com.university.libsys.backend.utils.RequestType;
import com.university.libsys.backend.utils.UserRole;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LibsysRequestServiceImpl implements LibsysRequestService {

    private final RequestRepository requestRepository;
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public LibsysRequestServiceImpl(RequestRepository requestRepository, MessageService messageService, UserService userService) {
        this.requestRepository = requestRepository;
        this.messageService = messageService;
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
    public LibsysRequest approveRequest(@NotNull Long id) throws RequestNotFoundException, UserNotFoundException, AlreadyProcessedRequestException {
        final LibsysRequest requestToApprove = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
        if (!Objects.equals(requestToApprove.getRequestStatus(), RequestStatus.PENDING)) {
            throw new AlreadyProcessedRequestException(id);
        }
        mapRequest(requestToApprove, RequestStatus.APPROVED);
        return requestRepository.save(requestToApprove);
    }

    @Override
    public LibsysRequest rejectRequest(@NotNull Long id) throws RequestNotFoundException, UserNotFoundException, AlreadyProcessedRequestException {
        final LibsysRequest requestToReject = requestRepository.findById(id)
                .orElseThrow(() -> new RequestNotFoundException(id));
        if (!Objects.equals(requestToReject.getRequestStatus(), RequestStatus.PENDING)) {
            throw new AlreadyProcessedRequestException(id);
        }
        mapRequest(requestToReject, RequestStatus.REJECTED);
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

    private void mapRequest(@NotNull LibsysRequest request, @NotNull RequestStatus finalStatus) throws UserNotFoundException {
        final RequestType requestType = request.getRequestType();

        if (requestType.equals(RequestType.WRITER_ROLE)) {
            if (finalStatus.equals(RequestStatus.APPROVED)) {
                approveWriterRole(request);
            } else if (finalStatus.equals(RequestStatus.REJECTED)) {
                rejectWriterRole(request);
            }
        }
    }

    private void approveWriterRole(LibsysRequest request) throws UserNotFoundException {
        final User user = userService.getUserById(request.getRequestSenderID());
        user.setUserRole(UserRole.WRITER);
        userService.updateUser(request.getRequestSenderID(), user);
        request.setRequestStatus(RequestStatus.APPROVED);
        messageService.saveNewMessage(createMessageAboutRequestStatus(request));
    }

    private void rejectWriterRole(LibsysRequest request) {
        request.setRequestStatus(RequestStatus.REJECTED);
        messageService.saveNewMessage(createMessageAboutRequestStatus(request));
    }

    private Message createMessageAboutRequestStatus(LibsysRequest request) {
        final String messageText = String.format("Status of request with id %s was changed to: %s",
                request.getId(), request.getRequestStatus().name());
        return new Message(null, messageText, request.getRequestSenderID(), 2L, MessageStatus.UNREAD);
    }
}
