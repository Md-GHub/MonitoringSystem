package com.md.monitoringsystem.exception;

public class UnAuthorizedUser extends RuntimeException {
    public UnAuthorizedUser(String message) {
        super(message);
    }
}
