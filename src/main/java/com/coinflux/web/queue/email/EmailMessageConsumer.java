package com.coinflux.web.queue.email;

import com.coinflux.web.queue.email.dtos.EmailMessageDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class EmailMessageConsumer {

    @RabbitListener(
            queues = RabbitMQConstants.EMAIL_QUEUE,
            containerFactory = "rabbitListenerContainerFactory" // 👈 force Spring to use Jackson
    )
    public void consumeEmail(EmailMessageDTO message) {
        log.info("📧 Email received: {}", message);
        // TODO: Call EmailService to send real email
    }
}