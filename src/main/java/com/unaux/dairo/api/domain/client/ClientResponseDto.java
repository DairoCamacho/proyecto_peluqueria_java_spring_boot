package com.unaux.dairo.api.domain.client;

import java.time.LocalDate;

public record ClientResponseDto(
        int id,
        String name,
        String lastName,
        String phone,
        LocalDate birthday,
        String email) {
}
