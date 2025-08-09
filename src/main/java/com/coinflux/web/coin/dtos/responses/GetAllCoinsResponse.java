package com.coinflux.web.coin.dtos.responses;

import com.coinflux.web.coin.dtos.CoinDTO;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllCoinsResponse {
    private List<CoinDTO> coins;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;
}
