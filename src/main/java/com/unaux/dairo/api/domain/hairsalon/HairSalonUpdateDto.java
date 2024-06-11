package com.unaux.dairo.api.domain.hairsalon;

import jakarta.validation.constraints.NotNull;

public record HairSalonUpdateDto(
  @NotNull int id,
  String name,
  String phone,
  String address,
  String neighborhood,
  String city,
  String country,
  Boolean status
) {}
