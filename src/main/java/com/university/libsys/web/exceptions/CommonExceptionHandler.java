package com.university.libsys.web.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class CommonExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception commonExceptionHandler(Exception ignoredE) {
        return new Exception("Wrong data provided");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String resolveSizeExceededException(MaxUploadSizeExceededException ignoreE, Model model) {
        model.addAttribute("error", "Max file size exceeded, please upload not so large photos");
        return "pages/create_post";
    }
}
