package com.unaux.dairo.api.domain.employee;

import java.time.LocalDate;

public record EmployeeFindDto(
  int id_employee,
  int id_client,
  String name,
  String lastName,
  String phone,
  LocalDate birthday,
  String email,
  String position,
  LocalDate hireDate,
  LocalDate terminationDate,
  int hairSalonId,
  boolean status
) {
  public EmployeeFindDto(Employee employee) {
    this(
      employee.getId(),
      employee.getClient().getId(),
      employee.getClient().getName(),
      employee.getClient().getLastName(),
      employee.getClient().getPhone(),
      employee.getClient().getBirthday(),
      employee.getClient().getUser().getEmail(),
      employee.getPosition(),
      employee.getHireDate(),
      employee.getTerminationDate(),
      employee.getHairSalon().getId(),
      employee.isStatus()
    );
  }
}
