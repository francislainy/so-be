package com.francislainy.sobe.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String QUESTION_NOT_FOUND_EXCEPTION = "NO QUESTION FOUND WITH THIS ID";
    public static final String ENTITY_DOES_NOT_BELONG_TO_USER_EXCEPTION = "User does not have permission to edit this entity";

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<Object> handleQuestionNotFoundException(Exception ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityDoesNotBelongToUserException.class)
    public ResponseEntity<Object> handleEntityDoesNotBelongToUserException(Exception ex) {
        return new ResponseEntity<>(new ApiError(ex.getMessage(), HttpStatus.FORBIDDEN, LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }
}
