package com.unaux.dairo.api.infra.errors;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// la anotación @RestControllerAdvice la cual permite tratar los errores globalmente a nivel de controller en lugar de hacerlo en cada método en específico. La anotación @RestControllerAdvice actúa como un proxy para todos los controllers para interceptar las llamadas en caso que suceda alguna excepción (programación orientada a aspectos).
@RestControllerAdvice
public class ControllerExceptionHandler { // tratador de errores

  // Este método se ejecuta cuando se lanza una excepción de tipo EntityNotFoundException.
  // En este caso, simplemente devuelve una respuesta HTTP 404 (Not Found)
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<?> handlingError404() {
    return ResponseEntity.notFound().build();
  }

  // Este método se ejecuta cuando se lanza una excepción de tipo MethodArgumentNotValidException.
  // El método toma la excepción como parámetro y extrae los errores de validación de los campos.
  // Luego, crea una lista de objetos DataErrorValidationDto a partir de los errores de campo.
  // Finalmente, devuelve una respuesta HTTP 400 (Bad Request) con la lista de errores de validación.
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handlingError400(MethodArgumentNotValidException e) {
    var error = e
      .getFieldErrors()
      .stream()
      .map(DataErrorValidationDto::new)
      .toList();
    return ResponseEntity.badRequest().body(error);
  }

  //Esta clase es un registro (record) que representa un error de validación.
  // para presentar los errores de una forma más entendible.
  public record DataErrorValidationDto(String field, String error) {
    public DataErrorValidationDto(FieldError error) {
      this(error.getField(), error.getDefaultMessage());
    }
  }
}
