package com.coinflux.web.coin;

import com.coinflux.web.coin.enums.CoinType;
import com.coinflux.web.coin_exchange_rate.CoinExchangeRateEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coins", indexes = {
        @Index(name = "idx_symbol", columnList = "symbol", unique = true)
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoinEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String symbol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private CoinType type;

    @OneToMany(mappedBy = "coin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoinExchangeRateEntity> exchangeRates = new ArrayList<>();
}
