package com.unaux.dairo.api.domain.appointment;

import com.unaux.dairo.api.domain.client.Client;
import com.unaux.dairo.api.domain.employee.Employee;
import com.unaux.dairo.api.domain.service.Service;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "appointment")
public class Appointment {

  @Id
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "appointmentIdGenerator"
  ) // Cambiado a SEQUENCE
  @GenericGenerator(
    name = "appointmentIdGenerator",
    strategy = "com.unaux.dairo.api.domain.appointment.AppointmentIdGenerator"
  )
  private String id;

  @Column(name = "date", nullable = false)
  private LocalDate date;

  @Column(name = "time", nullable = false)
  private LocalTime time;

  @Column(name = "status", nullable = false, length = 45)
  private String status;

  @Column(name = "notes", length = 45)
  private String notes;

  @ManyToOne
  @JoinColumn(
    name = "service_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Service service;

  @ManyToOne
  @JoinColumn(
    name = "employee_id",
    referencedColumnName = "id",
    nullable = false
  )
  private Employee employee;

  @ManyToOne
  @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
  private Client client;

  public Appointment(
    AppointmentCreateDto appointmentCreateDto,
    Service service,
    Employee employee,
    Client client
  ) {
    this.date = appointmentCreateDto.date();
    this.time = appointmentCreateDto.time();
    this.status = "pending";
    this.notes = appointmentCreateDto.notes();
    this.service = service;
    this.employee = employee;
    this.client = client;
  }

  public void update(
    AppointmentUpdateDto appointmentUpdateDto,
    Service service,
    Employee employee
  ) {
    if (appointmentUpdateDto.date() != null) {
      setDate(appointmentUpdateDto.date());
    }
    if (appointmentUpdateDto.time() != null) {
      setTime(appointmentUpdateDto.time());
    }
    if (appointmentUpdateDto.status() != null) {
      setStatus(appointmentUpdateDto.status());
    }
    if (appointmentUpdateDto.notes() != null) {
      setNotes(appointmentUpdateDto.notes());
    }
    if (service.getId() > 0) {
      setService(service);
    }
    if (employee.getId() > 0) {
      setEmployee(employee);
    }
  }
}
