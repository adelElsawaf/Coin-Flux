package com.coinflux.web.user_coin_watchlist;

import com.coinflux.web.auth.annotations.LoggedInUser;
import com.coinflux.web.user.UserEntity;
import com.coinflux.web.user.dtos.UserDTO;
import com.coinflux.web.user_coin_watchlist.dtos.requests.CreateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.GetAllUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.requests.UpdateUserCoinWatchlistRequest;
import com.coinflux.web.user_coin_watchlist.dtos.responses.CreateUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.dtos.responses.GetAllUserCoinWatchlistResponse;
import com.coinflux.web.user_coin_watchlist.dtos.responses.UpdateUserCoinWatchlistResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/user/coin-watchlist")
@RequiredArgsConstructor
@Validated
public class UserCoinWatchlistController {

    private final UserCoinWatchlistService watchlistService;

    @PostMapping
    public ResponseEntity<CreateUserCoinWatchlistResponse> createUserCoinWatchlist(
            @Valid @RequestBody CreateUserCoinWatchlistRequest request,
            @LoggedInUser UserDTO currentUser) {

        CreateUserCoinWatchlistResponse response = watchlistService.createUserCoinWatchlist(request,currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<GetAllUserCoinWatchlistResponse> getAllForUser(
            @LoggedInUser UserDTO currentUser,
            @RequestParam(required = false) String coinSymbol,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        GetAllUserCoinWatchlistRequest request = GetAllUserCoinWatchlistRequest.builder()
                .coinSymbol(coinSymbol)
                .isActive(isActive)
                .page(page)
                .size(size)
                .build();

        return ResponseEntity.ok(watchlistService.getAllForUser(currentUser.getId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserCoinWatchlistResponse> updateUserCoinWatchlist(
            @PathVariable("id") Long id,
            @LoggedInUser UserDTO currentUser,
            @Valid @RequestBody UpdateUserCoinWatchlistRequest request) {

        UpdateUserCoinWatchlistResponse response = watchlistService.updateUserWatchCoin(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchlistItem(
            @PathVariable Long id,
            @LoggedInUser UserDTO currentUser) {
        watchlistService.deleteWatchlistItem(id, currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}