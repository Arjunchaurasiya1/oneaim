package com.company.neurolink.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle BadCredentialsException (Invalid login credentials)
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        // Providing more specific error message about bad credentials
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Authentication failed. The provided username or password is incorrect.");
    }

    // Handle validation errors (e.g., invalid user input)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String field = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });
        // Return a detailed message about the validation error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    // Handle FileNotFoundException (when file is not found)
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The requested file was not found. Please check the file path or name.");
    }

    // Handle IOException (general I/O errors)
    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(IOException ex) {
        // Detailed message for general I/O errors
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An I/O error occurred while processing the file. Please try again later.");
    }

    // Handle MultipartException (file upload errors)
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<String> handleMultipartException(MultipartException ex) {
        // Specific message for file upload issues like size limit or format issues
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("File upload failed. Please check the file format and size. Ensure the file is not too large.");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>("Runtime Exception: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. You do not have permission to perform this action.");
    }

    @ExceptionHandler(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<String> handleAuthenticationCredentialsNotFoundException(org.springframework.security.authentication.AuthenticationCredentialsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized. Please log in to perform this action.");
    }

    @ExceptionHandler(org.springframework.security.authentication.InsufficientAuthenticationException.class)
    public ResponseEntity<String> handleInsufficientAuthenticationException(org.springframework.security.authentication.InsufficientAuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized. Please provide valid authentication credentials.");
    }
  


}
