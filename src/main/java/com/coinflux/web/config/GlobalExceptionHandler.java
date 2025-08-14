package com.coinflux.web.config;

import com.coinflux.web.auth.exceptions.AuthException;
import com.coinflux.web.coin.exceptions.CoinNotFoundException;
import com.coinflux.web.exception.ErrorObject;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorObject> handleGeneralException(Exception ex) {
        ErrorObject error = ErrorObject.builder()
                .errorMsg(ex.getMessage())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorObject> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ErrorObject error = ErrorObject.builder()
                .errorMsg("Database constraint violation: " + ex.getMostSpecificCause().getMessage())
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorObject> handleAuthException(AuthException ex) {
        ErrorObject error = ErrorObject.builder()
                .errorMsg(ex.getMessage())
                .errorCode(HttpStatus.UNAUTHORIZED.value())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
