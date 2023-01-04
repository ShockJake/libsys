package com.university.libsys.web.exceptions;

import com.university.libsys.backend.exceptions.AlreadyExistingUserException;
import com.university.libsys.backend.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class UserExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(AlreadyExistingUserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AlreadyExistingUserException userAlreadyExistsHandle(AlreadyExistingUserException e) {
        return e;
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public UserNotFoundException userNotFoundHandler(UserNotFoundException e) {
        return e;
    }
}
