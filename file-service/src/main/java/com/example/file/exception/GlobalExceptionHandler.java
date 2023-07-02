package com.example.file.exception;

import com.example.file.dto.ErrorResponse;
import com.example.file.exception.file.BulkRecipientDownloadException;
import com.example.file.exception.file.BulkRecipientRegistrationException;
import com.example.file.exception.file.InvalidFileFormatException;
import com.example.file.exception.file.WorkbookCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(errors, BAD_REQUEST);
    }

    @ExceptionHandler(BulkRecipientRegistrationException.class)
    public ResponseEntity<Map<String, String>> handleBulkRecipientRegistrationException(BulkRecipientRegistrationException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }

    @ExceptionHandler({
            BulkRecipientDownloadException.class,
            WorkbookCreationException.class,
            InvalidFileFormatException.class,
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e, WebRequest request) {
        return generateDefaultErrorMessage(e, BAD_REQUEST, request);
    }

    private ResponseEntity<ErrorResponse> generateDefaultErrorMessage(Exception e, HttpStatus httpStatus, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .description(request.getDescription(false))
                .code(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
