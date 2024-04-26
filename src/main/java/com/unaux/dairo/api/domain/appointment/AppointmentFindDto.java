package com.unaux.dairo.api.domain.appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentFindDto(
  String id,
  LocalDate date,
  LocalTime time,
  String status,
  String notes,
  int service,
  int employee,
  int client
) {
  public AppointmentFindDto(Appointment appointment){
    this(
      appointment.getId(),
      appointment.getDate(),
      appointment.getTime(),
      appointment.getStatus(),
      appointment.getNotes(),
      appointment.getService().getId(),
      appointment.getEmployee().getId(),
      appointment.getClient().getId()
    );
  }
}
