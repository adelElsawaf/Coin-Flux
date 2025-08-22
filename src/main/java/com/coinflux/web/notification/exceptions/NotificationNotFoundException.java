package com.coinflux.web.notification.exceptions;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(Long id) {
        super("Notification with ID " + id + " not found.");
    }
}
