package com.university.libsys.web.controllers.management;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.exceptions.MessageNotFoundException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import com.university.libsys.backend.services.Message.MessageService;
import com.university.libsys.backend.services.User.UserService;
import com.university.libsys.backend.utils.MessageStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages_management")
public class MessageManagementController {

    private final Logger log = LoggerFactory.getLogger(MessageManagementController.class);
    private final MessageService messageService;
    private final UserService userService;

    @Autowired
    public MessageManagementController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PutMapping("/{id}")
    public Message markMessage(@PathVariable Long id, @RequestParam String status) throws MessageNotFoundException {
        log.debug(String.format("Marking message with id = %s and new status is %s", id, status));
        return messageService.updateMessageStatus(id, MessageStatus.valueOf(status));
    }

    @DeleteMapping("/{id}")
    public Message deleteMessage(@PathVariable Long id) throws MessageNotFoundException {
        log.debug(String.format("Deleting message with id = %s", id));
        return messageService.deleteMessage(id);
    }

    @DeleteMapping("/all")
    public String deleteAllMessages(Authentication authentication) throws UserNotFoundException {
        messageService.deleteMessagesForUser(userService.getUserByLogin(authentication.getName()).getUserID());
        return "{ \"result\": \"true\" }";
    }
}
