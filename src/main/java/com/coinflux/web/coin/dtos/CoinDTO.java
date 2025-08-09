package com.coinflux.web.coin.dtos;

import com.coinflux.web.coin.enums.CoinType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinDTO {
    private Long id;
    private String name;
    private String symbol;
    private CoinType type;
}
