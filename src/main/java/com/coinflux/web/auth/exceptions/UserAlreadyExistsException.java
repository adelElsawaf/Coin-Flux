package com.coinflux.web.auth.exceptions;

public class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException(String email) {
        super("User already exists with " + email);
    }
}