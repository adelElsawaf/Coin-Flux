package com.coinflux.web.coin_exchange_rate.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRateResponse {
    private String from;
    private String to;
    private Double rate;
}