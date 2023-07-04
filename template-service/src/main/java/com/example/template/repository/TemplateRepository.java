package com.example.template.repository;

import com.example.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByIdAndClientId(Long templateId, Long clientId);

    Boolean existsByIdAndRecipientIds_recipientId(Long templateId, Long recipientId);

    Boolean existsTemplateByClientIdAndTitle(Long clientId, String title);
}
