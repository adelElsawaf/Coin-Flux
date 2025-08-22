package com.coinflux.web.notification.dtos.responses;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllNotificationsResponse {
    private List<GetNotificationResponse> notifications;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
