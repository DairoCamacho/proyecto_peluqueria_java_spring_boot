package com.unaux.dairo.api.domain.user;

import jakarta.validation.constraints.NotNull;

public record UserUpdateDto(
  @NotNull int id,
  String email,
  boolean status,
  String password,
  String role,
  String newPassword,
  String confirmPassword
) {}
