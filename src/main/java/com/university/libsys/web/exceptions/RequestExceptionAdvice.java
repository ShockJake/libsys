package com.university.libsys.web.exceptions;

import com.university.libsys.backend.exceptions.AlreadyProcessedRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RequestExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(AlreadyProcessedRequestException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public AlreadyProcessedRequestException alreadyProceededRequestHandler(AlreadyProcessedRequestException e) {
        return e;
    }
}
