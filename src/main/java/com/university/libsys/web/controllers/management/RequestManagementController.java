package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.LibsysRequest;
import com.university.libsys.backend.exceptions.AlreadyProcessedRequestException;
import com.university.libsys.backend.exceptions.RequestNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Request.LibsysRequestService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request_management")
public class RequestManagementController {

    private final String APPROVE_ACTION = "APPROVE";
    private final String REJECT_ACTION = "REJECT";

    private final LibsysRequestService requestService;

    public RequestManagementController(LibsysRequestService requestService) {
        this.requestService = requestService;
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
        if (APPROVE_ACTION.equals(action)) {
            return requestService.approveRequest(id);
        } else if (REJECT_ACTION.equals(action)) {
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
