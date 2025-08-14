package com.coinflux.web.auth.exceptions;

public class TokenGenerationException extends AuthException {
    public TokenGenerationException(String reason) {
        super("Failed to generate JWT token: " + reason);
    }
}