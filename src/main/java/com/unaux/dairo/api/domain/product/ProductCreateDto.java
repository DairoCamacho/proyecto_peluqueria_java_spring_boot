package com.unaux.dairo.api.domain.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ProductCreateDto(
  @NotBlank String name,
  @NotNull int price,
  @NotNull LocalTime duration,
  @NotNull int hairSalonId
) {}
