package com.unaux.dairo.api.domain.employee;

import java.time.LocalDate;

public record EmployeeFindDto(
  int id,
  String position,
  LocalDate hireDate,
  LocalDate terminationDate,
  int hairSalonId
) {
  public EmployeeFindDto(Employee employee) {
    this(
      employee.getClient().getId(),
      employee.getPosition(),
      employee.getHireDate(),
      employee.getTerminationDate(),
      employee.getHairSalon().getId()
    );
  }
}
