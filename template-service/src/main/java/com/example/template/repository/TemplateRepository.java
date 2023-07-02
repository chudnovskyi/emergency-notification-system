package com.example.template.repository;

import com.example.template.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByIdAndClientId(Long templateId, Long clientId);

//    Boolean existsByIdAndRecipientsId(Long templateId, Long recipientId);

    Boolean existsTemplateByClientIdAndTitle(Long clientId, String title);

//    @Query("""
//            SELECT DISTINCT recipient.id
//            FROM Template t
//            JOIN t.recipients recipient
//            WHERE
//            t.id = :templateId
//                AND
//            t.clientId = :clientId
//            """)
//    List<Long> findRecipientIdsByTemplateIdAndClientId(
//            @Param("templateId") Long templateId,
//            @Param("clientId") Long clientId
//    );
}
