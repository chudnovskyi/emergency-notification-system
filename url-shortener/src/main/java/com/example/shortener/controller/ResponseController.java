package com.example.shortener.controller;

import com.example.shortener.model.request.NotificationOptionRequest;
import com.example.shortener.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responses")
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping("/")
    public ResponseEntity<Long> create(
            @RequestBody @Valid NotificationOptionRequest request
    ) {
        return ResponseEntity.status(CREATED).body(responseService.createResponse(request));
    }
}
