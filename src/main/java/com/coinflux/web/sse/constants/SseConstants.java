package com.coinflux.web.sse.constants;

/**
 * Constants used across the SSE module.
 * These are static values, not expected to change between environments.
 */
public final class SseConstants {

    private SseConstants() {
        // prevent instantiation
    }

    /** Default SSE connection timeout (1 hour). */
    public static final long SSE_TIMEOUT_MS = 60 * 60 * 1000;

    /** Heartbeat interval (25 seconds). */
    public static final long SSE_HEARTBEAT_MS = 10 * 1000;
}
