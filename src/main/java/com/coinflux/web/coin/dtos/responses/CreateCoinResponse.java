package com.coinflux.web.coin.dtos.responses;

import com.coinflux.web.coin.enums.CoinType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCoinResponse {
    private Long id;
    private String name;
    private String symbol;
    private CoinType type;
}
