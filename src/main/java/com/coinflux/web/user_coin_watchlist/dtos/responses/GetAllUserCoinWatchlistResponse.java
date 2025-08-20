package com.coinflux.web.user_coin_watchlist.dtos.responses;


import com.coinflux.web.user_coin_watchlist.dtos.UserCoinWatchlistDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUserCoinWatchlistResponse {
    private List<UserCoinWatchlistDTO> items;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}