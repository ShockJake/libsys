package com.university.libsys.backend.services.Message;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.exceptions.MessageNotFoundException;
import com.university.libsys.backend.utils.MessageStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MessageService {

    List<Message> getMessagesByReceiverID(@NotNull Long id);

    void saveNewMessage(@NotNull Message message);

    Message deleteMessage(@NotNull Long id) throws MessageNotFoundException;

    Message updateMessageStatus(@NotNull Long id, @NotNull MessageStatus messageStatus) throws MessageNotFoundException;

    void deleteMessagesForUser(@NotNull Long id);
}
