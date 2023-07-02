package com.example.security.repository;

import com.example.security.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByClient_Id(Long id);

    Optional<Token> findByJwt(String jwt);
}
