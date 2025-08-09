package com.coinflux.web.coin;

import com.coinflux.web.coin.enums.CoinType;
import jakarta.persistence.*;
import lombok.*;

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
}
