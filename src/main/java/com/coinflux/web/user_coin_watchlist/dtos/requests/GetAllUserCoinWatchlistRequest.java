package com.coinflux.web.user_coin_watchlist.dtos.requests;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUserCoinWatchlistRequest {
    private String coinSymbol;
    private Boolean isActive;
    private Integer page = 0;
    private Integer size = 10;
}
