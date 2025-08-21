package com.coinflux.web.coin_exchange_rate.scheduled;

import com.coinflux.web.coin_exchange_rate.CoinExchangeRateService;
import com.coinflux.web.queue.coin_exchange_rate.CoinExchangeRateMessageProducer;
import com.coinflux.web.queue.coin_exchange_rate.dtos.CoinExchangeRateMessageDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinExchangeRateScheduler {

    private final CoinExchangeRateService coinExchangeRateService;
    private final CoinExchangeRateMessageProducer coinExchangeRateMessageProducer;

    @Scheduled(cron = "0,30 * * * * *")
    public void refreshExchangeRatesJob() {

        // Flush cache first
        coinExchangeRateService.flushExchangeRatesFromCache();

        long fetchStart = System.currentTimeMillis();
        var rates = coinExchangeRateService.fetchRatesFromCoinGecko();
        long start = System.nanoTime();

        rates.forEach((symbol, price) -> coinExchangeRateMessageProducer.sendExchangeRate(
                new CoinExchangeRateMessageDTO(symbol, price.getValue(), System.currentTimeMillis())
        ));
        long end = System.nanoTime();
        log.info("Rabbit MQ time {} ms", (end - start) / 1_000_000.0);


        coinExchangeRateService.saveRatesInDBAndCache(rates);
        long saveEnd = System.currentTimeMillis();

        log.info("Scheduled job finished: exchange rates refreshed. Total duration: {} ms",
                (saveEnd - fetchStart));
    }

}
