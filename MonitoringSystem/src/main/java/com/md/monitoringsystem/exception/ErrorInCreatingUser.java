package com.md.monitoringsystem.exception;

public class ErrorInCreatingUser extends RuntimeException {
    public ErrorInCreatingUser(String message) {
        super(message);
    }
}
