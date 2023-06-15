package com.example.recipient.repository;

import com.example.recipient.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {

    Optional<Recipient> findByEmailAndClient_Id(String email, Long clientId);

    Optional<Recipient> findByIdAndClient_Id(Long recipientId, Long clientId);

    List<Recipient> findAllByClient_Id(Long clientId);
}
