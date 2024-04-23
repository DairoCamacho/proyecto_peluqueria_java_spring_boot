package com.unaux.dairo.api.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserCreateDto(
  @NotBlank String email,
  @NotBlank String role,
  @NotBlank String password,
  @NotBlank String confirmPassword
) {}
