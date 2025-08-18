package com.coinflux.web.coin_exchange_rate;

import com.coinflux.web.coin_exchange_rate.dtos.responses.ExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exchange-rate")
@RequiredArgsConstructor
public class CoinExchangeRateController {

    private final CoinExchangeRateService coinExchangeRateService;

    @GetMapping
    public ResponseEntity<ExchangeRateResponse> getExchangeRate(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(coinExchangeRateService.getExchangeRate(from, to));
    }
}