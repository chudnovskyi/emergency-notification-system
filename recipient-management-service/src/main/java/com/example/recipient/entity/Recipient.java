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
                @UniqueConstraint(name = "email_unique", columnNames = {"client_id", "email"}),
                @UniqueConstraint(name = "telegram_id_unique", columnNames = {"client_id", "telegramId"}),
                @UniqueConstraint(name = "phone_number_unique", columnNames = {"client_id", "phoneNumber"})
        },
        indexes = {
                @Index(name = "recipients_idx_email", columnList = "email")
        }
)
public class Recipient implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Geolocation geolocation;

    @Column(nullable = false)
    private String email;
    private String telegramId;
    private String phoneNumber;

    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "client_id")
    private Client client;

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
