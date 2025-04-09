package com.kamilglazer.gosport.exception;

public class IllegalConnectionAction extends RuntimeException {
  public IllegalConnectionAction(String message) {
    super(message);
  }
}
