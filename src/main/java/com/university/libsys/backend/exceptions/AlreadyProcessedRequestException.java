package com.university.libsys.backend.exceptions;

import org.jetbrains.annotations.NotNull;

public class AlreadyProcessedRequestException extends Exception {

    public AlreadyProcessedRequestException(@NotNull Long id) {
        super(String.format("Request with id %s was already proceeded, if you want to approve it, ask the sender to make another request.", id));
    }
}
