package com.unaux.dairo.api.domain.appointment;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentUpdateDto(
  @NotNull int id,
  @NotNull LocalDate date,
  @NotNull LocalTime time,
  @NotNull int productId,
  @NotNull int employeeId,
  String notes
) {}
