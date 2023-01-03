package com.university.libsys.backend.exceptions;

import org.jetbrains.annotations.NotNull;

public class MessageNotFoundException extends Exception {

    public MessageNotFoundException(@NotNull Long id) {
        super(String.format("Message with id (%s) cannot be found", id));
    }
}
