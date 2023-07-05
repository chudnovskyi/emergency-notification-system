package com.example.template.entity;

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
        name = "templates",
        uniqueConstraints = {
                @UniqueConstraint(name = "templates_unq_clientId-title", columnNames = {"clientId", "title"}),
        }
)
public class Template implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;

    @Column(nullable = false)
    private String title;
    private String content;
    private String imageUrl; // TODO: Amazon S3

    @ToString.Exclude
    @Builder.Default
    @OneToMany(
            mappedBy = "template",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<RecipientId> recipientIds = new ArrayList<>();

    public Template addClient(Long clientId) {
        setClientId(clientId);
        return this;
    }

    public void addRecipient(Long recipientId) {
        recipientIds.add(
                RecipientId.builder()
                        .recipientId(recipientId)
                        .template(this)
                        .build()
        );
    }

    public Template removeRecipient(Long recipientId) {
        recipientIds.removeIf(
                recipient -> Objects.equals(recipient.getRecipientId(), recipientId)
        );
        return this;
    }
}
