package com.example.recipient.entity;

import com.example.recipient.model.TokenType;
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
@Table(name = "tokens")
public class Token implements BaseEntity<Long> {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne
    @JoinColumn(name = "client_id")
    public Client client;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    @Column(unique = true)
    public String jwt;

    public boolean revoked;
    public boolean expired;
}
