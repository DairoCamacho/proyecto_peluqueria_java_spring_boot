package com.unaux.dairo.api.domain.employee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

public record EmployeeCreateDto(
  @NotNull int clientId,
  @NotBlank String position,
  @NotNull LocalDate hireDate,
  @NotNull int hairSalonId,
  @NotNull Set<Integer> productsId
) {}
