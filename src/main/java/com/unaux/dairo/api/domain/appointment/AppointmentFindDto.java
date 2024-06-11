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
  int client
) {
  public AppointmentFindDto(Appointment appointment){
    this(
      appointment.getId(),
      appointment.getDateAppointment(),
      appointment.getTimeAppointment(),
      appointment.getConditionAppointment(),
      appointment.getNotes(),
      appointment.getProduct().getId(),
      appointment.getEmployee().getId(),
      appointment.getClient().getId()
    );
  }
}
