package com.unaux.dairo.api.domain.hairsalon;

import jakarta.validation.constraints.NotBlank;

public record HairSalonCreateDto(
  @NotBlank String name,
  @NotBlank String phone,
  @NotBlank String address,
  @NotBlank String neighborhood,
  @NotBlank String city,
  @NotBlank String country
) {}
