package com.coinflux.web.notification;

import com.coinflux.web.auth.annotations.LoggedInUser;
import com.coinflux.web.notification.dtos.requests.CreateNotificationRequest;
import com.coinflux.web.notification.dtos.requests.GetAllNotificationsRequest;
import com.coinflux.web.notification.dtos.responses.*;
import com.coinflux.web.user.UserService;
import com.coinflux.web.user.dtos.UserDTO;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@Slf4j
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

    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(@LoggedInUser UserDTO userDTO) {
        notificationService.markAllAsRead(userDTO.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }

    @PatchMapping("/read/{notificationId}")
    public ResponseEntity<?> markOneAsRead(
            @LoggedInUser UserDTO userDTO,
            @PathVariable Long notificationId
    ) {

        log.info(String.valueOf(userDTO.getId()));
        notificationService.markOneAsRead(userDTO.getId(), notificationId);
       return new ResponseEntity<>(HttpStatus.NO_CONTENT) ;
    }
}
