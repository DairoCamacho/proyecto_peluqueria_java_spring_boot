package com.unaux.dairo.api.domain.client;

import jakarta.validation.constraints.NotBlank;

public record ClientCreateDto(
        @NotBlank
        String name,
        @NotBlank
        String lastName,
        @NotBlank
        String phone,
        @NotBlank
        String birthday,
        @NotBlank
        String email,
        @NotBlank
        String password,
        @NotBlank
        String confirmPassword) {
}
