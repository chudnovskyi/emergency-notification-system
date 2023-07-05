package com.example.template.repository;

import com.example.template.entity.TemplateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateHistoryRepository extends JpaRepository<TemplateHistory, Long> {

    Optional<TemplateHistory> findByIdAndClientId(Long templateHistoryId, Long clientId);
}
