package com.kamilglazer.Zentro.exception;

public class UserWithThisEmailAlreadyExists extends RuntimeException {
    public UserWithThisEmailAlreadyExists(String message) {
        super(message);
    }
}
