package com.example.recipient.mapper;

import com.example.recipient.dto.request.TemplateRequest;
import com.example.recipient.dto.response.TemplateHistoryResponse;
import com.example.recipient.dto.response.TemplateResponse;
import com.example.recipient.entity.Template;
import com.example.recipient.entity.TemplateHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateMapper extends EntityMapper<Template, TemplateRequest, TemplateResponse> {

    TemplateHistory mapToTemplateHistory(TemplateHistoryResponse template);

    TemplateHistory mapToTemplateHistory(Template template);

    TemplateHistoryResponse mapToTemplateHistoryResponse(TemplateHistory templateHistory);
}
