package com.coinflux.web.queue.notification;

import com.coinflux.web.notification.NotificationService;
import com.coinflux.web.notification.dtos.requests.CreateNotificationRequest;
import com.coinflux.web.notification.dtos.responses.CreateNotificationResponse;
import com.coinflux.web.notification.dtos.responses.GetNotificationResponse;
import com.coinflux.web.notification.enums.NotificationType;
import com.coinflux.web.queue.notification.dtos.NotificationMessageDTO;
import com.coinflux.web.sse.SseService;
import com.coinflux.web.sse.dtos.SseNotificationDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import com.coinflux.web.sse.enums.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationMessageConsumer {

    private final NotificationService notificationService;
    private final SseService sseService;

    @RabbitListener(queues = RabbitMQConstants.NOTIFICATION_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void consumeNotification(NotificationMessageDTO message) {
        log.info("📲 Notification received from queue: {}", message);

        try {
            // 1) Build request for persistence
            CreateNotificationRequest request = CreateNotificationRequest.builder()
                    .userId(message.getUserId())
                    .title(buildTitleFromMessage(message))
                    .message(message.getMessage())
                    .type(NotificationType.PRICE_ALERT) // for now, all queue-based notifs are price alerts
                    .build();

            // 2) Persist via NotificationService
            CreateNotificationResponse saved = notificationService.createNotification(request);
            log.debug("💾 Notification persisted id={} for user={}", saved.getId(), saved.getUserId());

            // 3) Build SSE DTO
            SseNotificationDTO sseDto = SseNotificationDTO.builder()
                    .eventId(String.valueOf(saved.getId()))
                    .userId(saved.getUserId())
                    .type(EventType.NOTIFICATION)
                    .title(saved.getTitle())
                    .message(saved.getMessage())
                    .createdAt(LocalDateTime.ofInstant(
                            message.getTimestamp() > 0
                                    ? java.time.Instant.ofEpochMilli(message.getTimestamp())
                                    : java.time.Instant.now(),
                            ZoneId.systemDefault()))
                    .build();

            // 4) Push via SSE
            sseService.sendToUser(saved.getUserId(), sseDto);
            log.info("📡 SSE notification id={} pushed to user={}", saved.getId(), saved.getUserId());

        } catch (Exception ex) {
            log.error("❌ Failed to process notification message: {}. error={}", message, ex.getMessage(), ex);
            throw ex; // let RabbitMQ handle retry/DLQ
        }
    }

    private String buildTitleFromMessage(NotificationMessageDTO message) {
        if (message.getCoinSymbol() != null && !message.getCoinSymbol().isBlank()) {
            return message.getCoinSymbol().toUpperCase() + " Alert";
        }
        return "Notification";
    }
}
