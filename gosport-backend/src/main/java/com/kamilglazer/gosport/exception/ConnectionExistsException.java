package com.kamilglazer.gosport.exception;

public class ConnectionExistsException extends RuntimeException {
    public ConnectionExistsException(String message) {
        super(message);
    }
}
