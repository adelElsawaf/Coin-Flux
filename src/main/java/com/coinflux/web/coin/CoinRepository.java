package com.coinflux.web.coin;

import com.coinflux.web.coin.enums.CoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<CoinEntity, Long> {
    Optional<CoinEntity> findBySymbolIgnoreCase(String symbol);
    Page<CoinEntity> findByType(CoinType type, Pageable pageable);
}
