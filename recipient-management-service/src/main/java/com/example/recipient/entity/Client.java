package com.example.recipient.entity;

import com.example.recipient.model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "clients",
        indexes = {
                @Index(name = "clients_idx_email", columnList = "email")
        }
)
public class Client implements UserDetails, BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String email;
    private String password;

    private String name;
    private String company;
    private String phoneNumber;
    private Address address;

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @JoinColumn(name = "client_id")
    private Set<Recipient> recipients = new HashSet<>();

    public Recipient addRecipient(Recipient recipient) {
        recipients.add(recipient);
        recipient.setClient(this);
        return recipient;
    }

    public Recipient removeRecipient(Recipient recipient) {
        recipients.remove(recipient);
        recipient.setClient(null);
        return recipient;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }
}
