package com.coinflux.web.config;

import com.coinflux.web.coin.exceptions.CoinNotFoundException;
import com.coinflux.web.exception.ErrorObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CoinNotFoundException.class)
    public ResponseEntity<ErrorObject> handleCoinNotFoundException(CoinNotFoundException ex) {
        ErrorObject error = ErrorObject.builder()
                .errorMsg(ex.getMessage())
                .errorCode(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
