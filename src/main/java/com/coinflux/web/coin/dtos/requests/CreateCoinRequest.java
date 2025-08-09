package com.coinflux.web.coin.dtos.requests;

import com.coinflux.web.coin.enums.CoinType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCoinRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String symbol;

    @NotNull
    private CoinType type;
}
