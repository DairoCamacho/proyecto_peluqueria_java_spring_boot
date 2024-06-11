package com.unaux.dairo.api.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateDto(
  @NotNull int id,
  @NotBlank String password,
  String email,
  String newPassword,
  String confirmPassword
) {}
