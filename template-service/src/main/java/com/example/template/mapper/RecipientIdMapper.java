package com.example.template.mapper;

import com.example.template.dto.kafka.TemplateRecipientKafka;
import com.example.template.entity.RecipientId;
import com.example.template.entity.Template;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        imports = {
                Template.class
        }
)
public interface RecipientIdMapper {

    @Mapping(target = "template", expression = "java(Template.builder().id(kafka.templateId()).build())")
    RecipientId mapToEntity(TemplateRecipientKafka kafka);
}
