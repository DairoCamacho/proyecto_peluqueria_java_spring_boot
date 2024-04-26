package com.unaux.dairo.api.domain.service;

import java.time.LocalTime;

public record ServiceResponseDto(
  int id,
  String name,
  int price,
  LocalTime duration,
  int hairSalonId
) {}
