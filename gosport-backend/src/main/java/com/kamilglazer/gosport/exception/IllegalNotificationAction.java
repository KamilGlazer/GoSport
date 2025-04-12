package com.kamilglazer.gosport.exception;

public class IllegalNotificationAction extends RuntimeException {
    public IllegalNotificationAction(String message) {
        super(message);
    }
}
