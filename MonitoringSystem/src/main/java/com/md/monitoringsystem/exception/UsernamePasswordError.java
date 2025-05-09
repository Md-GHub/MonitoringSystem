package com.md.monitoringsystem.exception;

public class UsernamePasswordError extends RuntimeException {
    public UsernamePasswordError(String message) {
        super(message);
    }
}
