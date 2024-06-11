package com.unaux.dairo.api.domain.appointment;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class AppointmentIdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object entity) {
    Appointment appointment = (Appointment) entity;

    String employee = String.valueOf(appointment.getEmployee().getId());
    String client = String.valueOf(appointment.getClient().getId());
    String date = appointment.getDateAppointment().toString();
    String time = appointment.getTimeAppointment().toString();

    String id = employee + "--" + client + "--" +date + "--" + time;

    return id;
  }
}
