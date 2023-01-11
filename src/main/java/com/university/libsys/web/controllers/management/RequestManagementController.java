package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.LibsysRequest;
import com.university.libsys.backend.exceptions.AlreadyProcessedRequestException;
import com.university.libsys.backend.exceptions.RequestNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.Request.LibsysRequestService;
import com.university.libsys.web.util.MessageUtil;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request_management")
public class RequestManagementController {

    private final String APPROVE_ACTION = "APPROVE";
    private final String REJECT_ACTION = "REJECT";

    private final LibsysRequestService requestService;
    private final MessageService messageService;

    public RequestManagementController(LibsysRequestService requestService, MessageService messageService) {
        this.requestService = requestService;
        this.messageService = messageService;
    }

    @GetMapping("/{id}")
    public LibsysRequest getLibsysRequest(@PathVariable Long id) throws RequestNotFoundException {
        return requestService.getRequestByID(id);
    }

    @PostMapping("/create_request")
    public LibsysRequest createRequest(Authentication authentication, @RequestParam String type) throws UserNotFoundException {
        return requestService.createRequest(authentication.getName(), type);
    }

    @PatchMapping("/{id}")
    public LibsysRequest manageRequest(@PathVariable Long id, @RequestParam String action) throws RequestNotFoundException, UserNotFoundException, AlreadyProcessedRequestException {
        final LibsysRequest request = requestService.getRequestByID(id);
        if (APPROVE_ACTION.equals(action)) {
            messageService.saveNewMessage(MessageUtil.getApprovedRequestMessage(request.getRequestSenderID(), request.getRequestType().requestType));
            return requestService.approveRequest(id);
        } else if (REJECT_ACTION.equals(action)) {
            messageService.saveNewMessage(MessageUtil.getRejectRequestMessage(request.getRequestSenderID(), request.getRequestType().requestType));
            return requestService.rejectRequest(id);
        } else {
            throw new IllegalArgumentException(String.format("Cannot proceed method: %s", action));
        }
    }

    @DeleteMapping("/{id}")
    public LibsysRequest deleteRequest(@PathVariable Long id) throws RequestNotFoundException {
        return requestService.deleteRequest(id);
    }
}
