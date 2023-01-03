package com.university.libsys.web.exceptions;

import com.university.libsys.backend.exceptions.PostNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PostExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String userNotFoundHandler(PostNotFoundException e) {
        return e.getMessage();
    }
}
