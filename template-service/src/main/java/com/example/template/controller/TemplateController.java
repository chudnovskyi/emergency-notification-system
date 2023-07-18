package com.example.template.controller;

import com.example.template.dto.request.TemplateRequest;
import com.example.template.dto.response.TemplateResponse;
import com.example.template.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping("/")
    @Operation(summary = "create a Template")
    public ResponseEntity<TemplateResponse> create(
            @RequestHeader Long clientId,
            @RequestBody @Valid TemplateRequest request
    ) {
        return ResponseEntity.status(CREATED).body(templateService.create(clientId, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "get a Template by ID")
    public ResponseEntity<TemplateResponse> get(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(OK).body(templateService.get(clientId, templateId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "delete a Template by ID")
    public ResponseEntity<Boolean> delete(
            @RequestHeader Long clientId,
            @PathVariable("id") Long templateId
    ) {
        return ResponseEntity.status(OK).body(templateService.delete(clientId, templateId));
    }
}
