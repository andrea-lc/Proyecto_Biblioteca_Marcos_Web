package com.bibliot.gestion_biblioteca.config;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarValidacion(MethodArgumentNotValidException ex) {
        List<String> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": "
                        + (error.getDefaultMessage() != null ? error.getDefaultMessage() : "valor inválido"))
                .toList();
        return Map.of("message", "Revisa los datos del formulario.", "errors", errores);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> manejarEstado(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of("message", ex.getReason() != null ? ex.getReason() : "No se pudo completar la operación."));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarJson(HttpMessageNotReadableException ex) {
        return Map.of("message", "El JSON enviado no es válido. Revisa los datos del formulario.");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarRestricciones(DataIntegrityViolationException ex) {
        return Map.of("message", "Los datos ya existen o no cumplen las restricciones de la base de datos.");
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> manejarNegocio(RuntimeException ex) {
        return Map.of("message", ex.getMessage() != null ? ex.getMessage() : "No se pudo completar la operación.");
    }
}
