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

    private final CoinExchangeRateService service;

    @Scheduled(cron = "0,30 * * * * *") // Every minute
    public void fetchAndSaveRatesJob() {
        log.info("Scheduled job started: fetching and saving coin exchange rates.");

        long fetchStart = System.currentTimeMillis();
        var rates = service.fetchRatesFromCoinGecko();
        long fetchEnd = System.currentTimeMillis();
        log.info("Fetch time: {} ms", fetchEnd - fetchStart);

        long saveStart = System.currentTimeMillis();
        service.saveRates(rates);
        long saveEnd = System.currentTimeMillis();
        log.info("Save time: {} ms", saveEnd - saveStart);

        log.info("Scheduled job finished: exchange rates updated. Total duration: {} ms",
                (saveEnd - fetchStart));
    }

}
