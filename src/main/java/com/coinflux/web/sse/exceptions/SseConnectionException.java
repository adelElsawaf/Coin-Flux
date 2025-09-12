package com.coinflux.web.sse.exceptions;

public class SseConnectionException extends RuntimeException {
    public SseConnectionException(String message) {
        super(message);
    }
    public SseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
