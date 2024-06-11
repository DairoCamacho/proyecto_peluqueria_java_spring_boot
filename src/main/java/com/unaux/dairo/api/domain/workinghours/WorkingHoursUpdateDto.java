package com.unaux.dairo.api.domain.workinghours;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record WorkingHoursUpdateDto(
  @NotNull int id,
  LocalDateTime startDate,
  LocalDateTime endDate
) {}
