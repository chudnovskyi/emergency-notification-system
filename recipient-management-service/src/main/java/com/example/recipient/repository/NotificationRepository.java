package com.example.recipient.repository;

import com.example.recipient.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findByIdAndClient_Id(Long notificationId, Long clientId);
}
