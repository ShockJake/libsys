package com.university.libsys.backend.exceptions;

import org.jetbrains.annotations.NotNull;

public class PostNotFoundException extends Exception {

    public PostNotFoundException(@NotNull Long id) {
        super(String.format("Post with id %s cannot be found", id));
    }
}
