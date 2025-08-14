package com.coinflux.web.coin_exchange_rate.client.coin_gecko;

import com.coinflux.web.coin_exchange_rate.client.coin_gecko.dtos.ExchangeRatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "coinGeckoClient", url = "https://api.coingecko.com/api/v3")
public interface CoinGeckoClient {

    @GetMapping("/exchange_rates")
    ExchangeRatesResponse getExchangeRates();
}