package com.coinflux.web.user_coin_watchlist.exceptions;

public class DuplicateWatchlistItemException extends RuntimeException {
    public DuplicateWatchlistItemException(String message) {
        super(message);
    }
}