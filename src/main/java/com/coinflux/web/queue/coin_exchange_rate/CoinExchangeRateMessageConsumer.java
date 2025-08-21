package com.coinflux.web.queue.coin_exchange_rate;


import com.coinflux.web.queue.coin_exchange_rate.dtos.CoinExchangeRateMessageDTO;
import com.coinflux.web.shared.constants.RabbitMQConstants;
import com.coinflux.web.user_coin_watchlist.UserCoinWatchlistService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CoinExchangeRateMessageConsumer {

    private final UserCoinWatchlistService userCoinWatchlistService;

    @RabbitListener(queues = RabbitMQConstants.COIN_EXCHANGE_RATES_QUEUE)
    public void consume(CoinExchangeRateMessageDTO message) {
        userCoinWatchlistService.evaluateUserWatchlistRules(message);

    }

}
