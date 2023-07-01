package com.example.recipient.service;

import com.example.recipient.dto.request.GeolocationRequest;
import com.example.recipient.dto.request.RecipientRequest;
import com.example.recipient.entity.Geolocation;
import com.example.recipient.entity.Recipient;
import com.example.recipient.exception.file.BulkRecipientDownloadException;
import com.example.recipient.exception.file.BulkRecipientRegistrationException;
import com.example.recipient.exception.file.WorkbookCreationException;
import com.example.recipient.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileService {

    private final RecipientRepository recipientRepository;
    private final RecipientService recipientService;
    private final MessageSourceService message;

    public Boolean bulkRegistration(Long clientId, MultipartFile file) {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new WorkbookCreationException(message.getProperty("file.xlsx.format"));
        }
        Map<String, String> error = new HashMap<>();
        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum() - 1; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                break;
            }
            try {
                recipientService.register(
                        clientId,
                        RecipientRequest.builder()
                                .name(row.getCell(1).toString())
                                .email(row.getCell(2).toString())
                                .phoneNumber(row.getCell(3).toString())
                                .telegramId(row.getCell(4).toString())
                                .geolocation(
                                        GeolocationRequest.builder()
                                                .latitude(Double.parseDouble(row.getCell(5).toString()))
                                                .longitude(Double.parseDouble(row.getCell(6).toString()))
                                                .build()
                                )
                                .build()
                );
            } catch (RuntimeException e) {
                error.put(row.getCell(2).toString(), e.getMessage());
            }
        }

        if (!error.isEmpty()) {
            throw new BulkRecipientRegistrationException(error);
        }

        return true;
    }

    public byte[] downloadXlsx(Long clientId) {
        List<Recipient> recipients = recipientRepository.findAllByClient_Id(clientId);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Recipients");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Email");
            headerRow.createCell(3).setCellValue("Phone Number");
            headerRow.createCell(4).setCellValue("Telegram ID");
            headerRow.createCell(5).setCellValue("Latitude");
            headerRow.createCell(6).setCellValue("Longitude");

            for (int i = 0; i < recipients.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Recipient recipient = recipients.get(i);
                row.createCell(0).setCellValue(recipient.getId());
                row.createCell(1).setCellValue(recipient.getName());
                row.createCell(2).setCellValue(recipient.getEmail());
                row.createCell(3).setCellValue(recipient.getPhoneNumber());
                row.createCell(4).setCellValue(recipient.getTelegramId());
                Geolocation geolocation = recipient.getGeolocation();
                row.createCell(5).setCellValue(geolocation.getLatitude());
                row.createCell(6).setCellValue(geolocation.getLongitude());
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new BulkRecipientDownloadException(e.getMessage());
        }
    }
}
