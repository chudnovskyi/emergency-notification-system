package com.example.recipient.exception;

import com.example.recipient.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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
            RecipientNotFoundException.class,
            ClientNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e, WebRequest request) {
        return generateDefaultErrorMessage(e, NOT_FOUND, request);
    }

    @ExceptionHandler({
            RecipientRegistrationException.class,
            ClientBadCredentialsException.class,
            BulkRecipientDownloadException.class,
            WorkbookCreationException.class,
            AuthenticationException.class,
            ClientEmailAlreadyExists.class,
            InvalidFileFormatException.class
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
