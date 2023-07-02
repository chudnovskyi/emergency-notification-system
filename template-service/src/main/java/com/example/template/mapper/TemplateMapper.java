package com.example.template.mapper;

import com.example.template.dto.request.TemplateRequest;
import com.example.template.dto.response.TemplateHistoryResponse;
import com.example.template.dto.response.TemplateResponse;
import com.example.template.entity.Template;
import com.example.template.entity.TemplateHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TemplateMapper extends EntityMapper<Template, TemplateRequest, TemplateResponse> {

    TemplateHistory mapToTemplateHistory(Template template);

    TemplateHistoryResponse mapToTemplateHistoryResponse(TemplateHistory templateHistory);
}
