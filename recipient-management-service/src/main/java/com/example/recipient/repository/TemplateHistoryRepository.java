package com.example.recipient.repository;

import com.example.recipient.entity.TemplateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateHistoryRepository extends JpaRepository<TemplateHistory, Long> {

}
