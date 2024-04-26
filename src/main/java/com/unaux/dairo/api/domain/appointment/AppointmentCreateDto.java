package com.unaux.dairo.api.domain.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentCreateDto(
  @NotNull LocalDate date,
  @NotNull LocalTime time,
  @NotBlank String notes,
  @NotNull int service,
  @NotNull int employee,
  @NotNull int client
) {}
