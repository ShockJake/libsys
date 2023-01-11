package com.university.libsys.backend.exceptions;

import org.jetbrains.annotations.NotNull;

public class AlreadyExistingUserException extends Exception {

    public AlreadyExistingUserException(@NotNull String login) {
        super(String.format("Login %s is already occupied", login));
    }
}
