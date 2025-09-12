package com.coinflux.web.sse;

import com.coinflux.web.sse.dtos.SseNotificationDTO;
import com.coinflux.web.sse.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {

    /** userId -> sink for SSE events */
    private final Map<Long, Sinks.Many<ServerSentEvent<SseNotificationDTO>>> userSinks = new ConcurrentHashMap<>();

    /**
     * Subscribe to SSE for a specific user.
     */
    public Flux<ServerSentEvent<SseNotificationDTO>> subscribe(Long userId) {
        Sinks.Many<ServerSentEvent<SseNotificationDTO>> sink = Sinks.many().multicast().onBackpressureBuffer();
        userSinks.put(userId, sink);
        log.info("User {} subscribed to SSE (active connections: {})", userId, userSinks.size());

        return sink.asFlux()
                .doFinally(signal -> {
                    userSinks.remove(userId);
                    log.info("SSE connection closed for user {}. Active: {}", userId, userSinks.size());
                });
    }

    /**
     * Send a notification to a specific user.
     */
    public void sendToUser(Long userId, SseNotificationDTO dto) {
        Sinks.Many<ServerSentEvent<SseNotificationDTO>> sink = userSinks.get(userId);
        if (sink != null) {
            sink.tryEmitNext(ServerSentEvent.builder(dto)
                    .id(dto.getEventId())
                    .event(dto.getType().toString())
                    .build());
        } else {
            log.debug("📭 No active SSE connection for user {}, skipping push", userId);
        }
    }

    /**
     * Send heartbeat to all connected users.
     */
    public void sendHeartbeat() {
        log.debug("💓 Sending heartbeat to {} users", userSinks.size());
        userSinks.forEach((userId, sink) -> {
            SseNotificationDTO hb = SseNotificationDTO.builder()
                    .eventId("hb-" + System.currentTimeMillis())
                    .userId(userId)
                    .type(EventType.HEART_BEAT)
                    .title("heartbeat")
                    .message("💓")
                    .createdAt(LocalDateTime.now())
                    .build();

            sink.tryEmitNext(ServerSentEvent.builder(hb)
                    .event("heartbeat")
                    .build());
        });
    }
}
