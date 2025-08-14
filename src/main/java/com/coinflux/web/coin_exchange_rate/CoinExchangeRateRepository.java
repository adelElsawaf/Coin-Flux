package com.coinflux.web.coin_exchange_rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinExchangeRateRepository extends JpaRepository<CoinExchangeRateEntity, Long> {
}