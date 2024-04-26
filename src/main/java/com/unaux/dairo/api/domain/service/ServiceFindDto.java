package com.unaux.dairo.api.domain.service;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record ServiceFindDto(
  @NotNull int id,
  String name,
  int price,
  LocalTime duration,
  int hairSalonId
) {
  public ServiceFindDto(Service service) {
    this(
      service.getId(),
      service.getName(),
      service.getPrice(),
      service.getDuration(),
      service.getHairSalon().getId()
    );
  }
}
