package com.example.template.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private String title;

    private String content;

    private String imageUrl;

    public TemplateHistory addClient(Long clientId) {
        setClientId(clientId);
        return this;
    }
}
