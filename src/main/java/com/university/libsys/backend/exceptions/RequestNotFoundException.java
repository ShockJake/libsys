package com.university.libsys.backend.exceptions;

import org.jetbrains.annotations.NotNull;

public class RequestNotFoundException extends Exception {

    public RequestNotFoundException(@NotNull Long id) {
        super(String.format("Cannot find the request with id %s", id));
    }
}
