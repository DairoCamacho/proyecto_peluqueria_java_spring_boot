package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

public record ClientResponseDto(
  int id,
  LocalDate birthday,
  String lastName,
  String name,
  String phone,
  String type,
  String email
) {}
