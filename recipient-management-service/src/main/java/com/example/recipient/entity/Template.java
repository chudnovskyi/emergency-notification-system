package com.example.recipient.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "templates",
        uniqueConstraints = {
                @UniqueConstraint(name = "title_unique", columnNames = {"client_id", "title"}),
        }
)
public class Template implements BaseEntity<Long> { // TODO: user can respond to notification (template)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String content;
    private String imageUrl; // TODO: Amazon S3

    @ManyToOne(
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH
            }
    )
    @JoinColumn(name = "client_id")
    private Client client;

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
