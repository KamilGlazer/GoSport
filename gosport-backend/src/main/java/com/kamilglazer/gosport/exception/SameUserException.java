package com.kamilglazer.gosport.exception;

public class SameUserException extends RuntimeException {
    public SameUserException(String message) {
        super(message);
    }
}
