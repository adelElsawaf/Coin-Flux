package com.coinflux.web.coin_exchange_rate.mappers;

import com.coinflux.web.coin.CoinEntity;
import com.coinflux.web.coin_exchange_rate.CoinExchangeRateEntity;
import com.coinflux.web.coin_exchange_rate.client.coin_gecko.dtos.RateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", imports = { java.time.LocalDateTime.class })
public interface CoinExchangeRateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "coin", source = "coin")
    @Mapping(target = "value", source = "dto.value")
    @Mapping(target = "fetchedAt", expression = "java(LocalDateTime.now())")
    CoinExchangeRateEntity toEntity(RateDTO dto, CoinEntity coin);
}
