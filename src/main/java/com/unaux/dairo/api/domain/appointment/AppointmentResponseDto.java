package com.unaux.dairo.api.domain.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponseDto(
  String id,
  LocalDate date,
  LocalTime time,
  String status,
  String notes,
  int service,
  int employee,
  int client
) {}
