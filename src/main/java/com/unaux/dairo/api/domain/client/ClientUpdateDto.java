package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record ClientUpdateDto(
  @NotNull int id,
  LocalDate birthday,
  String lastName,
  String name,
  String phone
) {}
