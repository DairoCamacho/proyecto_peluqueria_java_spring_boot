package com.unaux.dairo.api.domain.workinghours;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record WorkingHoursUpdateDto(
  @NotNull String id,
  LocalDateTime startDate,
  LocalDateTime endDate,
  int employeeId
) {}
