package com.coinflux.web.shared.constants;

public class JWTConstants {
    public static final long ACCESS_TOKEN_EXPIRY_IN_MILLIS = 15 * 60 * 1000; // 15 min
    public static final long ACTIVATION_TOKEN_EXPIRY_IN_MILLIS = 2 * 60 * 1000;
    public static final long REFRESH_TOKEN_EXPIRY_IN_MILLIS = 7L * 24 * 60 * 60 * 1000; // 7 days
    private JWTConstants() {
        // prevent instantiation
    }
}
