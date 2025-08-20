package com.coinflux.web.user_coin_watchlist.dtos.requests;

import com.coinflux.web.user_coin_watchlist.enums.AlertRuleKeyword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserCoinWatchlistRequest {
    @NotBlank
    private String coinSymbol;

    @NotNull
    private AlertRuleKeyword alertRuleKeyword;

    @NotNull
    private BigDecimal targetValue;

}