package com.md.monitoringsystem.exception;

public class UserAlreadyActivated extends RuntimeException {
    public UserAlreadyActivated(String message) {
        super(message);
    }
}
