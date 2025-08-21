package com.coinflux.web.queue.coin_exchange_rate;


import com.coinflux.web.queue.coin_exchange_rate.dtos.CoinExchangeRateMessageDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoinExchangeRateMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendExchangeRate(CoinExchangeRateMessageDTO message) {
        rabbitTemplate.convertAndSend(
                RabbitMQConstants.COIN_EXCHANGE_RATES_EXCHANGE,
                RabbitMQConstants.COIN_EXCHANGE_RATES_ROUTING_KEY,
                message
        );
    }
}