package com.example.recipient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @ToString.Exclude
    @Builder.Default
    @OneToMany(
            mappedBy = "recipient",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TemplateId> templateIds = new ArrayList<>();

    public Recipient addClient(Long clientId) {
        setClientId(clientId);
        return this;
    }

    public void addTemplate(Long templateId) {
        templateIds.add(
                TemplateId.builder()
                        .templateId(templateId)
                        .recipient(this)
                        .build()
        );
    }

    public Recipient removeTemplate(Long templateId) {
        templateIds.removeIf(
                template -> Objects.equals(template.getTemplateId(), templateId)
        );
        return this;
    }
}
