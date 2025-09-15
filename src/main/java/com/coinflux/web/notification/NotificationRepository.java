package com.coinflux.web.notification;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long>, JpaSpecificationExecutor<NotificationEntity> {



    @Modifying(clearAutomatically = true)
    @Query("update NotificationEntity n set n.isRead = true, n.readedDate = CURRENT_TIMESTAMP where n.user.id = :userId and n.isRead = false")
    void markAllAsReadByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update NotificationEntity n set n.isRead = true, n.readedDate = CURRENT_TIMESTAMP where n.id = :id and n.user.id = :userId and n.isRead = false")
    void markOneAsReadByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}
