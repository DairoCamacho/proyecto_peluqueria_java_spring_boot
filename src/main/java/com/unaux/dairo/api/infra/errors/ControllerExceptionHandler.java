package com.unaux.dairo.api.infra.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler { // tratador de errores

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handlingError404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlingError400(MethodArgumentNotValidException e){
        var error = e.getFieldErrors().stream().map(DataErrorValidationDto::new).toList();
        return ResponseEntity.badRequest().body(error);
    }

    public record DataErrorValidationDto(String field, String error){
        public DataErrorValidationDto(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
