package com.example.template.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
// TODO: integrate with options
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "templates_history")
public class TemplateHistory implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long clientId;
    private Long responseId;

    @Column(nullable = false)
    private String title;

    private String content;

    private String imageUrl;

    public TemplateHistory addClient(Long clientId) {
        setClientId(clientId);
        return this;
    }
}
