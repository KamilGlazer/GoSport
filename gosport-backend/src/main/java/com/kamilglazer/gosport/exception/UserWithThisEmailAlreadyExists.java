package com.kamilglazer.gosport.exception;

public class UserWithThisEmailAlreadyExists extends RuntimeException {
    public UserWithThisEmailAlreadyExists(String message) {
        super(message);
    }
}
