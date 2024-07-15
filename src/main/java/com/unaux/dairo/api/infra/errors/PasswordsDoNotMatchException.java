package com.unaux.dairo.api.infra.errors;

public class PasswordsDoNotMatchException extends RuntimeException {
  public PasswordsDoNotMatchException(String message) {
    super(message);
  }
}