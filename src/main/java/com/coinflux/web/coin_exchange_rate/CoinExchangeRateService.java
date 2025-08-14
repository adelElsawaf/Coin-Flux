package com.coinflux.web.coin_exchange_rate;

import com.coinflux.web.coin.CoinEntity;
import com.coinflux.web.coin_exchange_rate.client.coin_gecko.CoinGeckoClient;
import com.coinflux.web.coin_exchange_rate.client.coin_gecko.dtos.RateDTO;
import com.coinflux.web.coin_exchange_rate.mappers.CoinExchangeRateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.coinflux.web.coin.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinExchangeRateService {

    private final CoinGeckoClient coinGeckoClient;
    private final CoinExchangeRateRepository exchangeRateRepository;
    private final CoinRepository coinRepository;
    private final CoinExchangeRateMapper mapper;

    public Map<String, RateDTO> fetchRatesFromCoinGecko() {
        log.info("Fetching exchange rates from CoinGecko...");
        var response = coinGeckoClient.getExchangeRates();
        return response.getRates();
    }

    @Transactional
    public void saveRates(Map<String, RateDTO> rates) {
        rates.forEach((symbol, rateDTO) -> {
            CoinEntity coin = coinRepository.findBySymbolIgnoreCase(symbol).orElse(null);
            if (coin != null) {
                CoinExchangeRateEntity rate = mapper.toEntity(rateDTO, coin);
                exchangeRateRepository.save(rate);
            }
        });
        log.info("Exchange rates saved successfully.");
    }
}