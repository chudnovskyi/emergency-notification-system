package com.example.notification.repository;

import com.example.notification.entity.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndClientId(Long notificationId, Long clientId);

    @Query("""
            SELECT n
            FROM Notification n
            WHERE
            n.clientId = :clientId AND
            (
                n.status = 'R'
                    OR
                n.status = 'N' AND n.createdAt < :newDateTime
                    OR
                n.status = 'P' AND n.createdAt  < :pendingDateTime
            )
            """)
    List<Notification> findNotificationsByStatusAndCreatedAt(
            @Param("clientId") Long clientId,
            @Param("pendingDateTime") LocalDateTime pendingDateTime,
            @Param("newDateTime") LocalDateTime newDateTime,
            Pageable pageable
    );
}
