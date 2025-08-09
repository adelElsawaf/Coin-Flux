package com.coinflux.web.coin.dtos.requests;

import com.coinflux.web.coin.enums.CoinType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCoinsRequest {
    private CoinType type;
    private Integer page = 0;
    private Integer size = 10;
}
