package com.coinflux.web.user_coin_watchlist;

import com.coinflux.web.coin.CoinEntity;
import com.coinflux.web.user.UserEntity;
import com.coinflux.web.user_coin_watchlist.enums.AlertRuleKeyword;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_coin_watchlist",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_coin", columnNames = {"user_id", "coin_symbol"})
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoinWatchlistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_watchlist_user")
    )
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "coin_symbol",
            referencedColumnName = "symbol",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_watchlist_coin")
    )
    private CoinEntity coin;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "alert_rule" , nullable = false )
    private AlertRuleKeyword alertRuleKeyword;

    @Column(name = "target_value",nullable = false)
    private BigDecimal targetValue;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


}
