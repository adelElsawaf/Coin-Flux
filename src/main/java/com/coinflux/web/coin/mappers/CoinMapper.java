package com.coinflux.web.coin.mappers;

import com.coinflux.web.coin.CoinEntity;
import com.coinflux.web.coin.dtos.CoinDTO;
import com.coinflux.web.coin.dtos.requests.CreateCoinRequest;
import com.coinflux.web.coin.dtos.responses.GetCoinResponse;
import  com.coinflux.web.coin.dtos.responses.CreateCoinResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CoinMapper {
    CoinMapper INSTANCE = Mappers.getMapper(CoinMapper.class);

    // Entity ↔ DTO
    CoinDTO toDTO(CoinEntity entity);
    CoinEntity toEntity(CoinDTO dto);

    // CreateCoinRequest → Entity
    CoinEntity toEntity(CreateCoinRequest request);

    // Entity → CreateCoinResponse
    CreateCoinResponse toCreateResponse(CoinEntity entity);

    // Entity → GetCoinResponse
    @Mapping(target = "coin", source = ".") // map entire entity to coin DTO
    GetCoinResponse toGetResponse(CoinEntity entity);}
