package com.coinflux.web.queue.coin_exchange_rate.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinExchangeRateMessageDTO {
    private String coinSymbol;
    private BigDecimal currentPrice;
    private Long timestamp;
}