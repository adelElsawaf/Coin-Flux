package com.coinflux.web.notification.dtos.requests;

import com.coinflux.web.notification.enums.NotificationType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private NotificationType type;
}
