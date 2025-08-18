package com.coinflux.web.coin_exchange_rate.scheduled;

import com.coinflux.web.coin_exchange_rate.CoinExchangeRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CoinExchangeRateScheduler {

    private final CoinExchangeRateService coinExchangeRateService;

    @Scheduled(cron = "0,30 * * * * *")
    public void refreshExchangeRatesJob() {

        // Flush cache first
        coinExchangeRateService.flushExchangeRatesFromCache();

        long fetchStart = System.currentTimeMillis();
        var rates = coinExchangeRateService.fetchRatesFromCoinGecko();

        coinExchangeRateService.saveRatesInDBAndCache(rates);
        long saveEnd = System.currentTimeMillis();

        log.info("Scheduled job finished: exchange rates refreshed. Total duration: {} ms",
                (saveEnd - fetchStart));
    }

}
