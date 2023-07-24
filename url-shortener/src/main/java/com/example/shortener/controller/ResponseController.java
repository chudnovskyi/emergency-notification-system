package com.example.shortener.controller;

import com.example.shortener.dto.response.UrlsResponse;
import com.example.shortener.model.request.NotificationOptionsRequest;
import com.example.shortener.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responses")
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping("/")
    public ResponseEntity<Long> create(
            @RequestBody @Valid NotificationOptionsRequest request
    ) {
        return ResponseEntity.status(CREATED).body(responseService.createResponse(request));
    }

    @PostMapping("/generate/{id}")
    public ResponseEntity<UrlsResponse> generate(
            @PathVariable("id") Long responseId
    ) {
        return ResponseEntity.status(CREATED).body(responseService.generate(responseId));
    }
}
