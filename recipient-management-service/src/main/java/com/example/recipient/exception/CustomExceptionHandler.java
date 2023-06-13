package com.example.recipient.exception;

import com.example.recipient.dto.response.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BulkRecipientRegistrationException.class)
    public ResponseEntity<Map<String, String>> handleBulkRecipientRegistrationException(BulkRecipientRegistrationException e) {
        return new ResponseEntity<>(e.getErrors(), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDaoConstraintExceptions(SQLException e) {
        return generateDefaultErrorMessage(e, BAD_REQUEST);
    }

    @ExceptionHandler(RecipientNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(Exception e) {
        return generateDefaultErrorMessage(e, NOT_FOUND);
    }

    @ExceptionHandler({
            RecipientRegistrationException.class,
            WorkbookCreationException.class,
            BulkRecipientDownloadException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception e) {
        return generateDefaultErrorMessage(e, BAD_REQUEST);
    }

    private ResponseEntity<ErrorResponse> generateDefaultErrorMessage(Exception e, HttpStatus httpStatus) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .message(e.getMessage())
                .code(httpStatus.value())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
