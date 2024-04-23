package com.unaux.dairo.api.domain.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientCreateDto(
  @NotNull LocalDate birthday,
  @NotBlank String confirmPassword,
  @NotBlank String email,
  @NotBlank String lastName,
  @NotBlank String name,
  @NotBlank String password,
  @NotBlank String phone
) {}
