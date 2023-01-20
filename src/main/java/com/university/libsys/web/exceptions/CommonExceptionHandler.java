package com.university.libsys.web.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.Arrays;

@ControllerAdvice
public class CommonExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(CommonExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception commonExceptionHandler(Exception ignoredE) {
        log.error(Arrays.toString(ignoredE.getStackTrace()));
        return new Exception("Wrong data provided");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String resolveSizeExceededException(MaxUploadSizeExceededException ignoreE, Model model) {
        model.addAttribute("error", "Max file size exceeded, please upload not so large photos");
        return "pages/create_post";
    }
}
