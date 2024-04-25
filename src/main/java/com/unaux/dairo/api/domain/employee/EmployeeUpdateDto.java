package com.unaux.dairo.api.domain.employee;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EmployeeUpdateDto(
  @NotNull int id,
  String position,
  LocalDate hireDate,
  LocalDate terminationDate,
  int hairSalonId
) {}
