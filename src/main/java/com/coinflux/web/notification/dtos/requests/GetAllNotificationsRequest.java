package com.coinflux.web.notification.dtos.requests;

import com.coinflux.web.notification.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllNotificationsRequest {
    private Boolean unreadOnly;
    private NotificationType type;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Integer page = 0;
    private Integer size = 20;
}
