package com.unaux.dairo.api.domain.user;

import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationDto(
  @NotBlank String email,
  @NotBlank String password
) {}
