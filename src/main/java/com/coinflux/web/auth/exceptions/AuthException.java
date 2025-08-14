package com.coinflux.web.auth.exceptions;

// Base Auth exception
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}