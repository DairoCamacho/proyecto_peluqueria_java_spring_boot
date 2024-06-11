package com.unaux.dairo.api.domain.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientCreateDto(
  @NotNull LocalDate birthday,
  @NotBlank String lastName,
  @NotBlank String name,
  @NotBlank String phone,
  @NotNull int userId
) {}
