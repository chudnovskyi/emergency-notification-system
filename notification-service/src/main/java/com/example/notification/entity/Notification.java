package com.example.notification.entity;

import com.example.notification.model.NotificationType;
import com.example.notification.model.NotificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.notification.model.NotificationStatus.PENDING;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;
    private Long recipientId;
    private Long templateHistoryId;

    private NotificationType type;
    private String credential;

    @Builder.Default
    private NotificationStatus status = PENDING;

    @Builder.Default
    private Integer retryAttempts = 0;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification setNotificationStatus(NotificationStatus notificationStatus) {
        setStatus(notificationStatus);
        return this;
    }

    public Notification incrementRetryAttempts() {
        setRetryAttempts(getRetryAttempts() + 1);
        return this;
    }

    public Notification addClient(Long clientId) {
        setClientId(clientId);
        return this;
    }

    public Notification addRecipient(Long recipientId) {
        setRecipientId(recipientId);
        return this;
    }

    public Notification addTemplateHistory(Long templateHistoryId) {
        setTemplateHistoryId(templateHistoryId);
        return this;
    }
}
