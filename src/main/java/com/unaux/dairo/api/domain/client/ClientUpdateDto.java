package com.unaux.dairo.api.domain.client;

import jakarta.validation.constraints.NotNull;

public record ClientUpdateDto(
  @NotNull int id,
  String birthday,
  String confirmPassword,
  String email,
  String lastName,
  String name,
  String newPassword,
  String password,
  String phone,
  String type
) {}
