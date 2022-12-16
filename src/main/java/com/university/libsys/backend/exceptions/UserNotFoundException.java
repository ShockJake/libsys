package com.university.libsys.backend.exceptions;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(Long id) {
        super(String.format("User with id %s cannot be found", id));
    }

    public UserNotFoundException(String login) {
        super(String.format("User with login %s cannot be found", login));
    }
}
