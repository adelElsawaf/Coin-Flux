package com.coinflux.web.user_coin_watchlist.dtos;
import com.coinflux.web.user_coin_watchlist.enums.AlertRuleKeyword;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoinWatchlistDTO {
    private Long id;
    private Long userId;
    private String coinSymbol;
    private boolean isActive;
    private AlertRuleKeyword alertRuleKeyword;
    private BigDecimal targetValue;
    private LocalDateTime createdAt;
}