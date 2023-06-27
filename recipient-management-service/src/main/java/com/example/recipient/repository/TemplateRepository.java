package com.example.recipient.repository;

import com.example.recipient.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByIdAndClient_Id(Long templateId, Long clientId);

    Boolean existsByIdAndRecipientsId(Long templateId, Long recipientId);

    Boolean existsTemplateByClient_IdAndTitle(Long clientId, String title);

    @Query("SELECT DISTINCT recipient.id FROM Template t JOIN t.recipients recipient WHERE t.id = :templateId")
    List<Long> findRecipientIdsByTemplateId(@Param("templateId") Long templateId);
}
