package com.coinflux.web.coin_exchange_rate;

import com.coinflux.web.coin.CoinEntity;
import com.coinflux.web.coin_exchange_rate.client.coin_gecko.CoinGeckoClient;
import com.coinflux.web.coin_exchange_rate.client.coin_gecko.dtos.RateDTO;
import com.coinflux.web.coin_exchange_rate.dtos.responses.ExchangeRateResponse;
import com.coinflux.web.coin_exchange_rate.mappers.CoinExchangeRateMapper;
import com.coinflux.web.shared.constants.RedisConstant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.coinflux.web.coin.CoinRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinExchangeRateService {

    private final CoinGeckoClient coinGeckoClient;
    private final CoinExchangeRateRepository exchangeRateRepository;
    private final CoinRepository coinRepository;
    private final CoinExchangeRateMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;


    public Map<String, RateDTO> fetchRatesFromCoinGecko() {
        long start = System.nanoTime();
        var response = coinGeckoClient.getExchangeRates();
        long end = System.nanoTime();
        log.info("Fetching from CoinGecko takes {}", (end - start) / 1_000_000.0);
        return response.getRates();
    }

    @Transactional
    public void saveRatesInDBAndCache(Map<String, RateDTO> rates) {
        List<CoinEntity> allCoins = coinRepository.findAll();
        long loopStart = System.nanoTime();
        rates.forEach((symbol, rateDTO) -> {
            CoinEntity coin = allCoins.stream()
                    .filter(c -> c.getSymbol().equalsIgnoreCase(symbol))
                    .findFirst()
                    .orElse(null);
            if (coin != null) {
                CoinExchangeRateEntity rate = mapper.toEntity(rateDTO, coin);
                // Save to DB
                exchangeRateRepository.save(rate);
                // Save to Redis
                String redisKey = RedisConstant.EXCHANGE_RATES_KEY + ":" + coin.getSymbol().toLowerCase();
                redisTemplate.opsForValue().set(redisKey, rate.getValue().toString());
            }
        });
        long loopEnd = System.nanoTime();
        log.info("Saving in DB & CACHE  {} ms", (loopEnd - loopStart) / 1_000_000.0);
    }

    public void flushExchangeRatesFromCache() {
        Set<String> keys = redisTemplate.keys(RedisConstant.EXCHANGE_RATES_KEY + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        } else {
            log.info("No keys found to flush from Redis cache");
        }
    }

    public ExchangeRateResponse getExchangeRate(String from, String to) {
        from = from.toLowerCase();
        to = to.toLowerCase();
        if (from.equals(to)) {
            return new ExchangeRateResponse(from, to, 1.0);
        }
        String fromRateStr = String.valueOf(redisTemplate.opsForValue().get(RedisConstant.EXCHANGE_RATES_KEY + ":" + from));
        String toRateStr = String.valueOf(redisTemplate.opsForValue().get(RedisConstant.EXCHANGE_RATES_KEY + ":" + to));
        if (fromRateStr == null) {
            throw new IllegalArgumentException("No cached rate found for: " + from);
        }
        if (toRateStr == null) {
            throw new IllegalArgumentException("No cached rate found for: " + to);
        }
        double fromRate = Double.parseDouble(fromRateStr);
        double toRate = Double.parseDouble(toRateStr);
        double conversionRate = toRate / fromRate;
        return new ExchangeRateResponse(from, to, conversionRate);
    }

}