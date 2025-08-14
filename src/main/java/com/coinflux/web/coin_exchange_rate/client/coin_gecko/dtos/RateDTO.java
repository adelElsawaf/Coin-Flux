package com.coinflux.web.coin_exchange_rate.client.coin_gecko.dtos;


import lombok.Data;
import java.math.BigDecimal;
@Data
public class RateDTO {
    private String name;
    private BigDecimal value;
}