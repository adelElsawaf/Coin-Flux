package com.coinflux.web.queue.coin_exchange_rate;

import com.coinflux.web.shared.constants.RabbitMQConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoinExchangeRateQueueConfig {

    @Bean
    public Queue coinExchangeRatesQueue() {
        return new Queue(RabbitMQConstants.COIN_EXCHANGE_RATES_QUEUE, true);
    }

    @Bean
    public DirectExchange coinExchangeRatesExchange() {
        return new DirectExchange(RabbitMQConstants.COIN_EXCHANGE_RATES_EXCHANGE);
    }

    @Bean
    public Binding coinExchangeRatesBinding(Queue coinExchangeRatesQueue, DirectExchange coinExchangeRatesExchange) {
        return BindingBuilder
                .bind(coinExchangeRatesQueue)
                .to(coinExchangeRatesExchange)
                .with(RabbitMQConstants.COIN_EXCHANGE_RATES_ROUTING_KEY);
    }
}