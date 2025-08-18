package com.coinflux.web.coin.dtos.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCoinRequest {
    private String symbol;
}
