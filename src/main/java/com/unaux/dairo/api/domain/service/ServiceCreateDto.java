package com.unaux.dairo.api.domain.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ServiceCreateDto(
  @NotBlank String name,
  @NotNull int price,
  @NotNull LocalTime duration,
  @NotNull int hairSalonId
) {}
