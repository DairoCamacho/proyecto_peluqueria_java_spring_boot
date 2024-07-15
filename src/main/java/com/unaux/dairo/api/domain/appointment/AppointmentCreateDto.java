package com.unaux.dairo.api.domain.appointment;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentCreateDto(
  @NotNull LocalDate date,
  @NotNull LocalTime time,
  String notes,
  @NotNull int productId,
  @NotNull int employeeId,
  @NotNull int clientId
) {}
