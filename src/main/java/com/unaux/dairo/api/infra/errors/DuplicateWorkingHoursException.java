package com.unaux.dairo.api.infra.errors;

public class DuplicateWorkingHoursException extends RuntimeException {
  public DuplicateWorkingHoursException(String message) {
    super(message);
  }
}
