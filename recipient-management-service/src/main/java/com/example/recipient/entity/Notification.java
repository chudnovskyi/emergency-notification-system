package com.example.recipient.entity;

import com.example.recipient.model.NotificationStatus;
import com.example.recipient.model.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

import static com.example.recipient.model.NotificationStatus.PENDING;

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

    private NotificationType type;

    private String credential;

    @Builder.Default
    private NotificationStatus status = PENDING;
    @Builder.Default
    private Integer retryAttempts = 0;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "recipient_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Recipient recipient;

    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "template_history_id")
    private TemplateHistory template;

    public Notification setNotificationStatus(NotificationStatus notificationStatus) {
        setStatus(notificationStatus);
        return this;
    }

    public Notification setNotificationType(NotificationType notificationType) {
        setType(notificationType);
        return this;
    }

    public Notification incrementRetryAttempts() {
        setRetryAttempts(getRetryAttempts() + 1);
        return this;
    }
}
