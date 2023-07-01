package com.example.recipient.controller;

import com.example.recipient.exception.file.InvalidFileFormatException;
import com.example.recipient.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/")
    @Operation(summary = "bulk Recipient registration with provided XLSX")
    public ResponseEntity<Boolean> bulkRegistration(
            @RequestHeader Long clientId,
            @RequestPart @Valid @NotNull(message = "{file.xlsx.not_null}") MultipartFile file
    ) {
        if (!Objects.equals(file.getContentType(), MediaType.APPLICATION_OCTET_STREAM_VALUE)
                && !Objects.equals(file.getContentType(), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            throw new InvalidFileFormatException("Invalid file format. Only XLSX files are allowed.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.bulkRegistration(clientId, file));
    }

    @GetMapping("/")
    @Operation(summary = "download XLSX with Recipients belonging to authorized Client")
    public ResponseEntity<ByteArrayResource> downloadXlsx(
            @RequestHeader Long clientId
    ) {
        byte[] data = fileService.downloadXlsx(clientId);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"output.xlsx\"")
                .body(resource);
    }
}
