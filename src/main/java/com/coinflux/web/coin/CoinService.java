package com.coinflux.web.coin;

import com.coinflux.web.coin.dtos.CoinDTO;
import com.coinflux.web.coin.dtos.requests.GetAllCoinsRequest;
import com.coinflux.web.coin.dtos.requests.CreateCoinRequest;
import com.coinflux.web.coin.dtos.responses.CreateCoinResponse;
import com.coinflux.web.coin.dtos.responses.GetAllCoinsResponse;
import com.coinflux.web.coin.dtos.responses.GetCoinResponse;
import com.coinflux.web.coin.exceptions.CoinNotFoundException;
import com.coinflux.web.coin.mappers.CoinMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoinService {

    private final CoinRepository coinRepository;
    private final CoinMapper coinMapper;

    public CreateCoinResponse createCoin(CreateCoinRequest request) {
        CoinEntity entity = coinMapper.toEntity(request);
        return coinMapper.toCreateResponse(coinRepository.save(entity));
    }

    public GetCoinResponse getCoinById(Long id) {
        CoinEntity entity = coinRepository.findById(id)
                .orElseThrow(() -> new CoinNotFoundException("Coin not found"));
        return coinMapper.toGetResponse(entity);
    }

    public GetCoinResponse getCoinBySymbol(String symbol) {
        CoinEntity entity = coinRepository.findBySymbolIgnoreCase(symbol)
                .orElseThrow(() -> new CoinNotFoundException("Coin not found"));
        return coinMapper.toGetResponse(entity);
    }

    public GetAllCoinsResponse getAllCoins(GetAllCoinsRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<CoinEntity> page = coinRepository.findByType(request.getType(), pageable);

        List<CoinDTO> coinDTOs = page.stream()
                .map(coinMapper::toDTO)
                .toList();

        return GetAllCoinsResponse.builder()
                .coins(coinDTOs)
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }

    public void deleteCoin(Long id) {
        if (!coinRepository.existsById(id)) {
            throw new RuntimeException("Coin not found");
        }
        coinRepository.deleteById(id);
    }
}
