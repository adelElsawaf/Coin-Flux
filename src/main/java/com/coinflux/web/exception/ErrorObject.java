package com.coinflux.web.exception;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorObject {
    private String errorMsg;
    private int errorCode;
    private LocalDateTime timestamp;
}
