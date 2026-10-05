package com.sunoza.controller;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.NoSuchElementException;
@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class) ResponseEntity<?> notFound(Exception e) { return ResponseEntity.status(404).body(Map.of("message",e.getMessage())); }
    @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<?> badRequest(Exception e) { return ResponseEntity.badRequest().body(Map.of("message", e.getMessage())); }
    @ExceptionHandler(AuthenticationException.class) ResponseEntity<?> unauthorized(AuthenticationException e) { return ResponseEntity.status(401).body(Map.of("message","Invalid email or password")); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<?> validation(MethodArgumentNotValidException e) { return ResponseEntity.badRequest().body(Map.of("message","Request validation failed")); }
}
