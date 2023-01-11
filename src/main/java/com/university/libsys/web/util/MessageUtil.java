package com.university.libsys.web.util;

import com.university.libsys.backend.entities.Message;
import com.university.libsys.backend.utils.MessageStatus;
import org.jetbrains.annotations.NotNull;

public class MessageUtil {
    private static final String CREATED_POST_MESSAGE = "Your post '%s' was created.";
    private static final String UPDATED_POST_MESSAGE = "Your post '%s' was updated.";
    private static final String DELETED_POST_MESSAGE = "Your post '%s' was deleted";
    private static final String CREATED_ACCOUNT_MESSAGE = "Thank you for creating account, your current role is Reader, you can request a writer role";
    private static final String REJECTED_REQUEST_MESSAGE = "Your request '%s' was rejected";
    private static final String APPROVED_REQUEST_MESSAGE = "Your request '%s' was approved";

    public static final Long ADMINISTRATION_ID = 1L;

    public static Message getCustomMessage(@NotNull String text, @NotNull Long receiverID, @NotNull Long senderID) {
        return new Message(null, text, receiverID, senderID, MessageStatus.UNREAD);
    }

    public static Message getMessageFromAdministration(@NotNull String text, @NotNull Long receiverID) {
        return new Message(null, text, receiverID, ADMINISTRATION_ID, MessageStatus.UNREAD);
    }

    public static Message getCreatedPostMessage(@NotNull Long receiverID, @NotNull String postHeader) {
        return getMessageFromAdministration(String.format(CREATED_POST_MESSAGE, postHeader), receiverID);
    }

    public static Message getUpdatedPostMessage(@NotNull Long receiverID, @NotNull String postHeader) {
        return getMessageFromAdministration(String.format(UPDATED_POST_MESSAGE, postHeader), receiverID);
    }

    public static Message getDeletedPostMessage(@NotNull Long receiverID, @NotNull String postHeader) {
        return getMessageFromAdministration(String.format(DELETED_POST_MESSAGE, postHeader), receiverID);
    }

    public static Message getCreatedAccountMessage(@NotNull Long receiverID) {
        return getMessageFromAdministration(CREATED_ACCOUNT_MESSAGE, receiverID);
    }

    public static Message getApprovedRequestMessage(@NotNull Long receiverID, @NotNull String requestType) {
        return getMessageFromAdministration(String.format(APPROVED_REQUEST_MESSAGE, requestType), receiverID);
    }

    public static Message getRejectRequestMessage(@NotNull Long receiverID, @NotNull String requestType) {
        return getMessageFromAdministration(String.format(REJECTED_REQUEST_MESSAGE, requestType), receiverID);
    }
}
