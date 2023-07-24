package com.example.shortener.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "urls")
public class Url {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ElementCollection(
            fetch = FetchType.EAGER
    )
    @CollectionTable(
            name = "url_option_mapping",
            joinColumns = @JoinColumn(name = "url_id")
    )
    @MapKeyColumn(
            name = "url_key"
    )
    @Column(name = "option")
    private Map<String, String> urlOptionMap = new HashMap<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
