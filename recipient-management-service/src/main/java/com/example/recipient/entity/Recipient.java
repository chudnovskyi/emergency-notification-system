package com.example.recipient.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "recipients",
        uniqueConstraints = {
                @UniqueConstraint(name = "recipients_unq_clientId-email", columnNames = {"clientId", "email"}),
                @UniqueConstraint(name = "recipients_unq_clientId-telegramId", columnNames = {"clientId", "telegramId"}),
                @UniqueConstraint(name = "recipients_unq_clientId-phoneNumber", columnNames = {"clientId", "phoneNumber"})
        },
        indexes = {
                @Index(name = "recipients_idx_email", columnList = "email")
        }
)
public class Recipient implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    private String name;
    private Geolocation geolocation;

    @Column(nullable = false)
    private String email;
    private String telegramId;
    private String phoneNumber;

    @Builder.Default
    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinTable(
            name = "template_recipient",
            joinColumns = @JoinColumn(name = "recipient_id"),
            inverseJoinColumns = @JoinColumn(name = "template_id")
    )
    private List<Template> templates = new ArrayList<>();
}
