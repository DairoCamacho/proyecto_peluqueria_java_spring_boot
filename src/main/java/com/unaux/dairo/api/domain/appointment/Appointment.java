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

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "appointment")
public class Appointment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

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
}
