package com.unaux.dairo.api.domain.service;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;

public record ServiceUpdateDto(
  @NotNull int id,
  String name,
  int price,
  LocalTime duration,
  int hairSalonId
) {}
