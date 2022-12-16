package com.university.libsys.backend.exceptions;

import org.jetbrains.annotations.NotNull;

public class AlreadyExistingUserException extends Exception {

    public AlreadyExistingUserException(@NotNull String login) {
        super(String.format("User with login %s already exists", login));
    }
}
