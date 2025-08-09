package com.coinflux.web.coin.exceptions;


public class CoinNotFoundException extends RuntimeException {
    public CoinNotFoundException(String message) {
        super(message);
    }
}
