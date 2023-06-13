package com.example.recipient.entity;

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
@Table(name = "recipients",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unique", columnNames = {"email"}),
                @UniqueConstraint(name = "telegram_id_unique", columnNames = {"telegramId"}),
                @UniqueConstraint(name = "phone_number_unique", columnNames = {"phoneNumber"})
        }
)
public class Recipient implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String telegramId;
    private String phoneNumber;

    private String name;
    private Geolocation geolocation;
}
