package com.example.template.entity;

import com.example.template.listener.RecipientIdListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Eventual Consistency | ManyToMany with recipient-service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "recipient_ids",
        uniqueConstraints = {
                @UniqueConstraint(name = "recipient_ids_unq_template-recipient", columnNames = {"template_id", "recipientId"})
        }
)
@EntityListeners(RecipientIdListener.class)
public class RecipientId implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Template template;

    private Long recipientId;
}
