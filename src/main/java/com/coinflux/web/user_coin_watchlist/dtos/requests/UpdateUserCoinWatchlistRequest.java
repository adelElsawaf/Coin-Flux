package com.coinflux.web.user_coin_watchlist.dtos.requests;


import com.coinflux.web.user_coin_watchlist.enums.AlertRuleKeyword;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserCoinWatchlistRequest {

    @NotNull(message = "isActive cannot be null")
    private Boolean isActive;

    @NotNull(message = "alertRuleKeyword cannot be null")
    private AlertRuleKeyword alertRuleKeyword;

    @NotNull(message = "targetValue cannot be null")
    private BigDecimal targetValue;
}