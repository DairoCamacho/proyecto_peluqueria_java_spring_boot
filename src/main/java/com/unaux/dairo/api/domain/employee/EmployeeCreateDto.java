package com.unaux.dairo.api.domain.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EmployeeCreateDto(
  @NotNull int id,
  @NotBlank String position,
  @NotNull LocalDate hireDate,
  @NotNull int hairSalonId
) {}
