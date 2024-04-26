package com.unaux.dairo.api.domain.appointment;

import java.io.Serializable;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class AppointmentIdGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session,Object entity) {
    Appointment appointment = (Appointment) entity;

    String client = String.valueOf(appointment.getClient().getId());
    String date = appointment.getDate().toString();
    String time = appointment.getTime().toString();

    String id = client + "--" + date + "--" + time;

    return id;
  }
}
