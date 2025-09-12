package com.coinflux.web.notification;

import com.coinflux.web.notification.dtos.requests.CreateNotificationRequest;
import com.coinflux.web.notification.dtos.requests.GetAllNotificationsRequest;
import com.coinflux.web.notification.dtos.responses.*;
import com.coinflux.web.user.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    @PostMapping
    public CreateNotificationResponse create(@RequestBody CreateNotificationRequest request) {
        return notificationService.createNotification(request);
    }

    @GetMapping("/{id}")
    public GetNotificationResponse getById(@PathVariable Long id) {
        return notificationService.getNotificationById(id);
    }

    @PostMapping("/search/{userId}")
    public GetAllNotificationsResponse getAll(
            @PathVariable Long userId,
            @RequestBody GetAllNotificationsRequest request
    ) {
        return notificationService.getAllNotifications(request, userId);
    }

    @PatchMapping("/{userId}/read-all")
    @ResponseStatus(NO_CONTENT)
    public void markAllAsRead(@PathVariable Long userId) {
        notificationService.markAllAsRead(userId);
    }

    @PatchMapping("/{userId}/read/{notificationId}")
    @ResponseStatus(NO_CONTENT)
    public void markOneAsRead(
            @PathVariable Long userId,
            @PathVariable Long notificationId
    ) {
        notificationService.markOneAsRead(userId, notificationId);
    }
}
