package com.coinflux.web.mail.exceptions;

public class MailFailedException extends RuntimeException {
    public MailFailedException(String message) {
        super(message);
    }

    public MailFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}