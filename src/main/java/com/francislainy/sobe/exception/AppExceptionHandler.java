package com.francislainy.sobe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String QUESTION_NOT_FOUND_EXCEPTION = "NO QUESTION FOUND WITH THIS ID";

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }
}
