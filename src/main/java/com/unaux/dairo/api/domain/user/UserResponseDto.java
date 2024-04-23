package com.unaux.dairo.api.domain.user;

public record UserResponseDto(
  int id,
  String email,
  boolean status,
  String role
) {}
