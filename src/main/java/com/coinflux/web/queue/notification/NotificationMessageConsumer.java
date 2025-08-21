package com.coinflux.web.queue.notification;

import com.coinflux.web.queue.notification.dtos.NotificationMessageDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NotificationMessageConsumer {

    @RabbitListener(queues = RabbitMQConstants.NOTIFICATION_QUEUE)
    public void consumeNotification(NotificationMessageDTO message) {
        log.info("📲 Notification received: {}", message);
        // TODO: Call NotificationService to send real-time event (SSE/WebSocket)
    }

}