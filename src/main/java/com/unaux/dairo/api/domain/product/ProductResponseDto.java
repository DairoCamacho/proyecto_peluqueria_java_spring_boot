package com.unaux.dairo.api.domain.product;

import java.time.LocalTime;

public record ProductResponseDto(
  int id,
  String name,
  int price,
  LocalTime duration,
  int hairSalonId,
  boolean status
) {}
