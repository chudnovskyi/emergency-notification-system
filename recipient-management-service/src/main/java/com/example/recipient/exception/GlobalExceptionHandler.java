package com.example.recipient.exception;

import com.example.recipient.dto.response.ErrorResponse;
import com.example.recipient.exception.client.ClientBadCredentialsException;
import com.example.recipient.exception.client.ClientEmailAlreadyExistsException;
import com.example.recipient.exception.client.ClientNotFoundException;
import com.example.recipient.exception.file.BulkRecipientDownloadException;
import com.example.recipient.exception.file.BulkRecipientRegistrationException;
import com.example.recipient.exception.file.InvalidFileFormatException;
import com.example.recipient.exception.file.WorkbookCreationException;
import com.example.recipient.exception.notification.NotificationMappingNotFoundException;
import com.example.recipient.exception.notification.NotificationNotFoundException;
import com.example.recipient.exception.recipient.RecipientNotFoundException;
import com.example.recipient.exception.recipient.RecipientRegistrationException;
import com.example.recipient.exception.template.TemplateCreationException;
import com.example.recipient.exception.template.TemplateNotFoundException;
import com.example.recipient.exception.template.TemplateRecipientsNotFound;
import com.example.recipient.exception.template.TemplateTitleAlreadyExistsException;
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

import static org.springframework.http.HttpStatus.*;

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
            ClientNotFoundException.class,
            TemplateNotFoundException.class,
            TemplateRecipientsNotFound.class,
            NotificationNotFoundException.class,
            NotificationMappingNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e, WebRequest request) {
        return generateDefaultErrorMessage(e, NOT_FOUND, request);
    }

    @ExceptionHandler({
            ClientEmailAlreadyExistsException.class,
            TemplateTitleAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleAlreadyExists(Exception e, WebRequest request) {
        return generateDefaultErrorMessage(e, CONFLICT, request);
    }

    @ExceptionHandler({
            RecipientRegistrationException.class,
            ClientBadCredentialsException.class,
            BulkRecipientDownloadException.class,
            WorkbookCreationException.class,
            AuthenticationException.class,
            InvalidFileFormatException.class,
            TemplateCreationException.class
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
