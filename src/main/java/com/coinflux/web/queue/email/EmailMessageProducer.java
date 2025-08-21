package com.coinflux.web.queue.email;

import com.coinflux.web.queue.email.dtos.EmailMessageDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailMessageDTO message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.EMAIL_EXCHANGE,
                RabbitMQConstants.EMAIL_ROUTING_KEY,
                message
        );
    }
}