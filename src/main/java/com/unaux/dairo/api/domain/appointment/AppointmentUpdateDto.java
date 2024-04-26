package com.unaux.dairo.api.domain.appointment;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentUpdateDto(
  @NotBlank String id,
  LocalDate date,
  LocalTime time,
  String status,
  String notes,
  int service,
  int employee,
  int client
) {}
