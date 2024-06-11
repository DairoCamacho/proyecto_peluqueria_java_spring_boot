package com.unaux.dairo.api.domain.product;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record ProductUpdateDto(
  @NotNull int id,
  String name,
  int price,
  LocalTime duration,
  int hairSalonId,
  Boolean status
) {}
