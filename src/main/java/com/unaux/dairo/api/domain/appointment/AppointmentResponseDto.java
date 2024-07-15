package com.unaux.dairo.api.domain.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentResponseDto(
  int id,
  LocalDate date,
  LocalTime time,
  String condition,
  String notes,
  int product,
  int employee,
  int client,
  boolean status
) {}
