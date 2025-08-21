package com.coinflux.web.queue.notification.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessageDTO {
    private Long userId;
    private String coinSymbol;
    private String message;
    private long timestamp;
}
