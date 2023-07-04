package com.example.recipient.entity;

import com.example.recipient.listeners.TemplateIdListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Eventual Consistency | ManyToMany with template-service
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "template_ids",
        uniqueConstraints = {
                @UniqueConstraint(name = "template_ids_unq_recipient-trample", columnNames = {"recipient_Id", "templateId"})
        }
)
@EntityListeners(TemplateIdListener.class)
public class TemplateId {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Recipient recipient;

    private Long templateId;
}
