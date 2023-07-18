package com.example.shortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "responses")
public class Response implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ElementCollection(
            targetClass = String.class,
            fetch = FetchType.EAGER
    )
    @CollectionTable(
            name = "options",
            joinColumns = @JoinColumn(name = "responses_id")
    )
    @Column(
            name = "option", nullable = false
    )
    private List<String> options = new ArrayList<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
