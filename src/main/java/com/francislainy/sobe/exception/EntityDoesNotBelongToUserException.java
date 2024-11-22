package com.francislainy.sobe.exception;

public class EntityDoesNotBelongToUserException extends RuntimeException {

    public EntityDoesNotBelongToUserException(String message) {
        super(message);
    }
}
