package com.coinflux.web.notification;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, JpaSpecificationExecutor<NotificationEntity> {

    @Modifying
    @Query("update NotificationEntity n set n.readAt = :now where n.user.id = :userId and n.readAt is null")
    int markAllAsRead(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Modifying
    @Query("update NotificationEntity n set n.readAt = :now where n.user.id = :userId  and n.id = :notificationId and n.readAt is null")
    void updateNotificationByUserIdAndNotificationId(Long userId, Long notificationId,@Param("now") LocalDateTime now);
}
