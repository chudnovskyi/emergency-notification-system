package com.example.recipient.repository;

import com.example.recipient.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByIdAndClient_Id(Long templateId, Long clientId);

    Boolean existsByIdAndRecipientsId(Long templateId, Long recipientId);

    Boolean existsTemplateByClient_IdAndTitle(Long clientId, String title);
}
