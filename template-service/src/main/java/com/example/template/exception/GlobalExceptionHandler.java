package com.example.template.exception;

import com.example.template.dto.response.ErrorResponse;
import com.example.template.exception.template.TemplateCreationException;
import com.example.template.exception.template.TemplateNotFoundException;
import com.example.template.exception.template.TemplateRecipientsNotFound;
import com.example.template.exception.template.TemplateTitleAlreadyExistsException;
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

    @ExceptionHandler({
            TemplateNotFoundException.class,
            TemplateRecipientsNotFound.class,
    })
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e, WebRequest request) {
        return generateDefaultErrorMessage(e, NOT_FOUND, request);
    }

    @ExceptionHandler({
            TemplateTitleAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleAlreadyExists(Exception e, WebRequest request) {
        return generateDefaultErrorMessage(e, CONFLICT, request);
    }

    @ExceptionHandler({
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
