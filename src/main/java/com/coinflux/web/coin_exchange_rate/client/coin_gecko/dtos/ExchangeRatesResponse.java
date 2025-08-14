package com.coinflux.web.coin_exchange_rate.client.coin_gecko.dtos;

import lombok.Data;
import java.util.Map;

@Data
public class ExchangeRatesResponse {
    private Map<String, RateDTO> rates;
}
