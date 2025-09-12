package com.coinflux.web.sse.dtos;

import com.coinflux.web.sse.enums.EventType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Unified SSE payload contract sent to the frontend.
 * Keep this stable as part of your public API between server and client.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SseNotificationDTO {

    /** Unique event id for the client (can be the notification id as string). */
    private String eventId;

    /** The target user id (for client-side filtering if needed). */
    private Long userId;

    /** A short event type, e.g. "NOTIFICATION", "SYSTEM", "HEARTBEAT". */
    private EventType type;

    /** Human-readable title (for notifications). */
    private String title;

    /** Message/body text. */
    private String message;

    /** Server timestamp. */
    private LocalDateTime createdAt;
}
