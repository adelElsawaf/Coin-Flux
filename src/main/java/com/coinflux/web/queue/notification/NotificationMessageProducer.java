package com.coinflux.web.queue.notification;

import com.coinflux.web.queue.notification.dtos.NotificationMessageDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class NotificationMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendNotification(NotificationMessageDTO message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.NOTIFICATION_EXCHANGE,
                RabbitMQConstants.NOTIFICATION_ROUTING_KEY,
                message
        );
    }
}