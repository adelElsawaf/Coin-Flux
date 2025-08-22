package com.coinflux.web.notification;

import com.coinflux.web.notification.dtos.requests.CreateNotificationRequest;
import com.coinflux.web.notification.dtos.requests.GetAllNotificationsRequest;
import com.coinflux.web.notification.dtos.responses.*;
import com.coinflux.web.notification.exceptions.NotificationNotFoundException;
import com.coinflux.web.notification.mappers.NotificationMapper;
import com.coinflux.web.notification.specifications.NotificationSpecification;
import com.coinflux.web.user.UserEntity;
import com.coinflux.web.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    public CreateNotificationResponse createNotification(CreateNotificationRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        NotificationEntity entity = NotificationEntity.builder()
                .user(user)
                .title(request.getTitle())
                .message(request.getMessage())
                .type(request.getType())
                .isRead(false)
                .build();

        NotificationEntity saved = notificationRepository.save(entity);
        return notificationMapper.toCreateResponse(saved);
    }

    public GetNotificationResponse getNotificationById(Long id) {
        NotificationEntity entity = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        return notificationMapper.toGetResponse(entity);
    }

    public GetAllNotificationsResponse getAllNotifications(GetAllNotificationsRequest request, Long userId) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), Sort.by("createdAt").descending());

        var spec = NotificationSpecification.filterBy(request, userId);
        Page<NotificationEntity> page = notificationRepository.findAll(spec, pageable);

        return GetAllNotificationsResponse.builder()
                .notifications(page.getContent().stream().map(notificationMapper::toGetResponse).toList())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber())
                .build();
    }
}
