package com.coinflux.web.queue.email;

import com.coinflux.web.shared.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailQueueConfig {

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(RabbitMQConstants.EMAIL_EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(RabbitMQConstants.EMAIL_QUEUE, true);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(RabbitMQConstants.EMAIL_ROUTING_KEY);
    }
}