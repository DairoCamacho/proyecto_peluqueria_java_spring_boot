package com.unaux.dairo.api.domain.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentFindDto(
  int id,
  LocalDate date,
  LocalTime time,
  String condition,
  String notes,
  int product,
  int employee,
  int client,
  Boolean status
) {
  public AppointmentFindDto(Appointment appointment){
    this(
      appointment.getId(),
      appointment.getDate(),
      appointment.getTime(),
      appointment.getCondition(),
      appointment.getNotes(),
      appointment.getProduct().getId(),
      appointment.getEmployee().getId(),
      appointment.getClient().getId(),
      appointment.isStatus()
    );
  }
}
