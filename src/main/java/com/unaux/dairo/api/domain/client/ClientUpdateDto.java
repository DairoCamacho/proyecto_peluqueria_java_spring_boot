package com.unaux.dairo.api.domain.client;

import jakarta.validation.constraints.NotNull;

public record ClientUpdateDto(
        @NotNull
        int id,
        String name,
        String lastName,
        String phone,
        String birthday,
        String type,
        String email,
        String password,
        String newPassword,
        String confirmPassword) {
}
