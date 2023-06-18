package com.example.recipient.controller;

import com.example.recipient.entity.Client;
import com.example.recipient.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/")
    @Operation(summary = "bulk Recipient registration with provided XLSX")
    public ResponseEntity<Boolean> bulkRegistration(
            @AuthenticationPrincipal Client client,
            @RequestPart MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.bulkRegistration(client, file));
    }

    @GetMapping("/")
    @Operation(summary = "download XLSX with Recipients belonging to authorized Client")
    public ResponseEntity<ByteArrayResource> downloadXlsx(
            @AuthenticationPrincipal Client client
    ) {
        byte[] data = fileService.downloadXlsx(client);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"output.xlsx\"")
                .body(resource);
    }
}
