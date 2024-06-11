package com.unaux.dairo.api.domain.employee;

import java.time.LocalDate;

public record EmployeeResponseDto(
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
) {}
