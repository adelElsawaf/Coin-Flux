package com.coinflux.web.user_coin_watchlist;

import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user_coin_watchlist.dtos.UserCoinWatchlistDTO;
import com.coinflux.web.user_coin_watchlist.dtos.requests.CreateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.GetAllUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.UpdateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.responses.CreateUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.dtos.responses.GetAllUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.dtos.responses.UpdateUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.exceptions.DuplicateWatchlistItemException;
import com.coinflux.web.user_coin_watchlist.exceptions.UserCoinWatchlistNotFoundException;
import com.coinflux.web.user_coin_watchlist.mappers.UserCoinWatchlistMapper;
import com.coinflux.web.user_coin_watchlist.specifications.UserCoinWatchlistSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCoinWatchlistService {
    private final UserCoinWatchlistRepository watchlistRepository;
    private final UserCoinWatchlistMapper mapper;

    @Transactional
    public CreateUserCoinWatchlistResponse createUserCoinWatchlist(CreateUserCoinWatchlistRequest request, UserDTO currentUser) {
        // Check if already exists
        watchlistRepository.findByUserIdAndCoinSymbol(currentUser.getId(), request.getCoinSymbol())
                .ifPresent(existing -> {
                    throw new DuplicateWatchlistItemException("Coin is already in watchlist");
                });
        // Map request to entity
        UserCoinWatchlistEntity entity = mapper.toEntity(request, currentUser.getId());

        // Save entity
        UserCoinWatchlistEntity saved = watchlistRepository.save(entity);

        // Build response
        return CreateUserCoinWatchlistResponse.builder()
                .watchItem(mapper.toDTO(saved))
                .build();
    }

    public GetAllUserCoinWatchlistResponse getAllForUser(Long userId, GetAllUserCoinWatchlistRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Specification<UserCoinWatchlistEntity> spec =
                UserCoinWatchlistSpecification.filterBy(request, userId);

        Page<UserCoinWatchlistEntity> page = watchlistRepository.findAll(spec, pageable);

        List<UserCoinWatchlistDTO> dtos = mapper.toDTOList(page.getContent());

        return mapper.toGetAllResponse(page, dtos);
    }


    @Transactional
    public UpdateUserCoinWatchlistResponse updateUserWatchCoin(Long watchlistId, UpdateUserCoinWatchlistRequest request) {
        UserCoinWatchlistEntity entity = watchlistRepository.findById(watchlistId)
                .orElseThrow(() -> new UserCoinWatchlistNotFoundException("Watchlist not found with ID: " + watchlistId));

        entity = mapper.toEntityFromUpdateRequest(request, entity);

        UserCoinWatchlistEntity saved = watchlistRepository.save(entity);

        return UpdateUserCoinWatchlistResponse.builder()
                .watchlist(mapper.toDTO(saved))
                .build();
    }

    @Transactional
    public void deleteWatchlistItem(Long id, Long userId) {
        UserCoinWatchlistEntity entity = watchlistRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new UserCoinWatchlistNotFoundException(
                        "Watchlist item not found for id: " + id + " and userId: " + userId));

        watchlistRepository.delete(entity);
    }



}
