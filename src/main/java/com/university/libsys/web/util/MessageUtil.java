package com.university.libsys.web.util;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.utils.MessageStatus;
import org.jetbrains.annotations.NotNull;

public class MessageUtil {

    public static final Long ADMINISTRATION_ID = 2L;

    public static Message createMessage(@NotNull String text, @NotNull Long receiverID, @NotNull Long senderID) {
        return new Message(null, text, receiverID, senderID, MessageStatus.UNREAD);
    }

    public static Message createMessageFromAdministration(@NotNull String text, @NotNull Long receiverID) {
        return new Message(null, text, receiverID, ADMINISTRATION_ID, MessageStatus.UNREAD);
    }
}
