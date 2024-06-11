package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

public record ClientResponseDto(
  int id,
  LocalDate birthday,
  String lastName,
  String name,
  String phone,
  boolean status,
  String type,
  int userId
) {}
