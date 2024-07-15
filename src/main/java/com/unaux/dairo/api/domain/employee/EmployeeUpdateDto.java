package com.unaux.dairo.api.domain.employee;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record EmployeeUpdateDto(
  @NotNull int id,
  LocalDate hireDate,
  String position,
  Boolean status,
  LocalDate terminationDate,
  int hairSalonId,
  Set<Integer> productsId
) {}
