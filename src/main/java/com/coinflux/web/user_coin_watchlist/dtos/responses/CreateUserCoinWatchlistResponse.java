package com.coinflux.web.user_coin_watchlist.dtos.responses;

import com.coinflux.web.user_coin_watchlist.dtos.UserCoinWatchlistDTO;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCoinWatchlistResponse {
    private UserCoinWatchlistDTO watchItem;
}
