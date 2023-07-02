package com.example.recipient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "recipient_id")
    )
    private List<Recipient> recipients = new ArrayList<>();

    public Recipient addRecipient(Recipient recipient) {
        recipients.add(recipient);
        return recipient;
    }

    public Recipient removeRecipient(Recipient recipient) {
        recipients.remove(recipient);
        return recipient;
    }
}
