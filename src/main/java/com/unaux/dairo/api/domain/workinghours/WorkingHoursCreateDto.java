package com.unaux.dairo.api.domain.workinghours;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record WorkingHoursCreateDto(
  @NotNull LocalDateTime startDate,
  @NotNull LocalDateTime endDate,
  @NotNull int employeeId
) {}
