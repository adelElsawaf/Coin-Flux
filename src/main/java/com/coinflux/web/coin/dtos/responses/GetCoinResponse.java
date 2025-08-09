package com.coinflux.web.coin.dtos.responses;

import com.coinflux.web.coin.dtos.CoinDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCoinResponse {
    private CoinDTO coin;
}
