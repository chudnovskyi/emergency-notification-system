package com.example.template.repository;

import com.example.template.entity.TemplateHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateHistoryRepository extends JpaRepository<TemplateHistory, Long> {

}
