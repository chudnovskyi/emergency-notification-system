package com.example.recipient.controller;

import com.example.recipient.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/")
    public ResponseEntity<Boolean> bulkRegistration(@RequestPart MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.bulkRegistration(file));
    }

    @GetMapping("/")
    public ResponseEntity<ByteArrayResource> downloadXlsx() {
        byte[] data = fileService.downloadXlsx();
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"output.xlsx\"")
                .body(resource);
    }
}
