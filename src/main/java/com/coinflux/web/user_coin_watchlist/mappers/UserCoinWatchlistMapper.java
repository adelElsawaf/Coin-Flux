package com.coinflux.web.user_coin_watchlist.mappers;

import com.coinflux.web.user_coin_watchlist.UserCoinWatchlistEntity;
import com.coinflux.web.user_coin_watchlist.dtos.UserCoinWatchlistDTO;
import com.coinflux.web.user_coin_watchlist.dtos.requests.CreateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.UpdateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.responses.GetAllUserCoinWatchlistResponse;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserCoinWatchlistMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "coin.symbol", source = "request.coinSymbol")
    @Mapping(target = "isActive", constant = "true")
    @Mapping(target = "targetValue", source = "request.targetValue")
    UserCoinWatchlistEntity toEntity(CreateUserCoinWatchlistRequest request, Long userId);


    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "coin.symbol", target = "coinSymbol")
    @Mapping(target = "isActive", source = "active")
    UserCoinWatchlistDTO toDTO(UserCoinWatchlistEntity entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "coin", ignore = true)
    UserCoinWatchlistEntity toEntityFromUpdateRequest(UpdateUserCoinWatchlistRequest request, @MappingTarget UserCoinWatchlistEntity entity);

    List<UserCoinWatchlistDTO> toDTOList(List<UserCoinWatchlistEntity> entities);

    default GetAllUserCoinWatchlistResponse toGetAllResponse(Page<UserCoinWatchlistEntity> page,
                                                             List<UserCoinWatchlistDTO> dtos) {
        return GetAllUserCoinWatchlistResponse.builder()
                .items(dtos)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }


}