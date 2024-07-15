package com.unaux.dairo.api.domain.user;

public record UserResponseDto(
  int id,
  String email,
  Role role,
  Boolean status
) {}
